import './style.scss';
import NavBar from "../../components/NavBar/NavBar";
import React, {useEffect, useRef, useState} from "react";
import useRequest from "../../utils/useRequest";
import type {ResponseType, ListItemType, CartSubmitArray, SubmitResponseType} from "./types";
import {message} from "../../utils/message";
import {useNavigate, useSearchParams} from "react-router-dom";
import {API_ENDPOINTS} from "../../config/api";

function Cart() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    
    // 检查是否从Detail页面跳转而来
    const fromDetail = searchParams.get('from') === 'detail';
    console.log("is from detail page:"+fromDetail);
    const productId = searchParams.get('productId');

    //loading data from backend server at the first time only
    const { request } = useRequest<ResponseType>({manual: true});
    const { request: submitRequest } = useRequest<SubmitResponseType>({manual: true});
    const [list, setList ] = useState<ListItemType[]>([]);
    useEffect(() => {
        request({
                url: API_ENDPOINTS.CART_PRODUCTS,
                method: 'GET'
            }).then((data)=>{
            const list = data.data;
            //fetch shop list data from response data
            const newList= list.map(shop =>{

                //added new return property 'selected' to every product
                const newCartList= shop.cartList.map(product => {
                    return {...product, selected: false}
                });
                return{ shopId:shop.shopId, shopName:shop.shopName, cartList: newCartList}
            });
            // console.log(newList);
            //passing newList to list
            setList(newList);
        }).catch((e)=>{
                console.log(e?.message);
                message(e.message);
            })
    }, [request]);


    function handleCountChange(shopId: string, productId: string, count: string) {
        //copy list
        const newList = [...list];
        //find the shop
        const shop = newList.find(shop => shop.shopId === shopId);
        shop?.cartList.forEach(product=>{
            if(product.productId === productId){
                // product.count = Number(value);
                console.log(count);
                const countNumber = +count;
                product.count = Number.isNaN(countNumber) ? 0 : countNumber;
            }
        });
        //use newList to replace list
        setList(newList);
    }

    function handleProductClick(shopId: string, productId: string) {
        //copy list
        const newList = [...list];
        //find the shop
        const shop = newList.find(shop => shop.shopId === shopId);
        let shopAllSelected = true;
        shop?.cartList.forEach(product=>{
            if(product.productId === productId){
              product.selected = !product.selected;
            }
            if(!product.selected){
                shopAllSelected = false;
            }
        });
        shop!.selected = shopAllSelected;
        //use newList to replace list
        setList(newList);
    }

    function handleShopSelectClick(shopId: string) {
        //copy list
        const newList = [...list];
        //find the shop
        const shop = newList.find(shop => shop.shopId === shopId);
        shop?.cartList.forEach(product=>{
            product.selected= true;
        });
        //use newList to replace list
        shop!.selected = true;
        setList(newList);
    }

    function handleSelectAllClick() {
        const newList = [...list];
        newList.forEach(shop=>{
            shop.selected = true;
            shop.cartList.forEach(product=>{
                product.selected = true;
            })
        });
        setList(newList);
    }

    const notSelectedShop = list.find(shop => !shop.selected);
    let count = 0;
    let totalPrice = 0;
    list.forEach(shop=>{
        shop.cartList.forEach(product=>{
            if(product.selected){
                count++;
                totalPrice += product.price * product.count;
            }
        })
    })


    function handleCartSubmit() {
        const params: CartSubmitArray = [];
        list.forEach(shop=>{
            shop.cartList.forEach(product=>{
                if(product.selected){
                    params.push({productId: product.productId, count: product.count})
                }
            })
        });
        if(params.length === 0){
            message('Select at least one product');
            return;
        }
        submitRequest({
            url: API_ENDPOINTS.ORDER_SUBMIT,
            method: 'POST',
            data: params
        }).then(response=>{
            // message('Please Confirm Your Order!');
            //if success, get orderId and navigate to order page
            const {orderId} = response.data;
            navigate(`/order/${orderId}`);
            console.log('Cart submit response:', response);
        }).catch((e)=>{
            message(e.message);
        })
        console.log(params);
    }



    // 返回Detail页面的函数
    function handleBackToDetail() {
        if (productId) {
            navigate(`/detail/${productId}`);
        } else {
            navigate(-1); // 如果没有productId，就返回上一页
        }
    }

    return (
        <div className="page cart-page">
            {/*show back icon depends on from Detail page or NaviBar */}
            <div className='title'>
                {fromDetail && (
                    <div className="iconfont back-icon" onClick={handleBackToDetail}>
                        &#xe6a9;
                    </div>
                )}
                Cart
            </div>

            {/*show every shop iterated by list*/}

            {
                list.map(shop=>{
                    return (

                        <div className='shop' key={shop.shopId}>
                            <div className='shop-title' onClick={()=>{handleShopSelectClick(shop.shopId)}}>
                                <div className={shop.selected? 'radio radio-active':'radio'}></div>
                                <span className='iconfont'>&#xe7ce;</span>{shop.shopName}
                            </div>
                            <div className='shop-products'>
                                {/*fetch cartList from shopList*/}
                                {
                                    shop.cartList.map(product=>{
                                        return (

                                            <div className='shop-product'
                                                 key={product.productId}
                                                 onClick={()=>{handleProductClick(shop.shopId, product.productId)}}
                                            >
                                                <div className={product.selected? 'radio radio-active':'radio'}></div>
                                                <img src={product.imgUrl}
                                                     alt={product.title}
                                                     className='shop-product-img'/>
                                                <div className='shop-product-content'>
                                                    <div className='shop-product-title'>
                                                        {product.title}
                                                    </div>


                                                    <div className='shop-product-kilo'>
                                                        {product.weight}
                                                    </div>
                                                    <div className='shop-product-price'>
                                                        <span className='shop-product-price-symbol'>&#36; </span>
                                                        {product.price}
                                                    </div>
                                                    <input  className='shop-product-count' 
                                                            onChange={(e)=>{handleCountChange(shop.shopId, product.productId,e.target.value)}} 
                                                            onClick={(e)=>e.stopPropagation()}
                                                            value={product.count}/>
                                                </div>
                                            </div>
                                        )
                                    })
                                }



                            </div>
                        </div>


                    )
                })
            }


            {/* show total price*/}
            <div className='total-price'>
                <div className='select-all' onClick={handleSelectAllClick}>
                    <div className={notSelectedShop? 'radio' : 'radio radio-active'}></div>
                    <div className='select-all-text' >Select All</div>
                </div>
                <div className='total'>
                    <span className='total-text'>Total:</span>
                    <div className='total-price-inner'>
                        <span className='total-price-inner-symbol'>&#36; </span>
                        {totalPrice.toFixed(2)}
                    </div>
                </div>
                <div className='check' onClick={handleCartSubmit}>Checkout({count})</div>

            </div>
            <NavBar activeName='cart'/>
        </div>
    )
}

export default Cart;