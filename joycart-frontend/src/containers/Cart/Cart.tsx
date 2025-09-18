import './style.scss';
import NavBar from "../../components/NavBar/NavBar";
import React, {useEffect, useRef, useState} from "react";
import useRequest from "../../utils/useRequest";
import type {ResponseType, ListItemType, CartSubmitArray, SubmitResponseType} from "./types";
import {message} from "../../utils/message";
import {useNavigate} from "react-router-dom";

function Cart() {
    const navigate = useNavigate();

    //loading data from backend server at the first time only
    const { request } = useRequest<ResponseType>({manual: true});
    const { request: submitRequest } = useRequest<SubmitResponseType>({manual: true});
    const [list, setList ] = useState<ListItemType[]>([]);
    useEffect(() => {
        request({
                url: '/cartProducts.json',
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
            url: '/cartSubmit.json',
            method: 'GET',
            // The original POST request code was implemented using Charles Proxy, and it is currently commented out to facilitate future conversion into a full-stack project.
            // method: 'POST',
            // data: params
        }).then(response=>{
            // message('Order submitted successfully');
            //if success, get orderId and navigate to order page
            const {orderId} = response.data;
            navigate(`/order/${orderId}`);
            console.log(response);
        }).catch((e)=>{
            message(e.message);
        })
        console.log(params);
    }



    return (
        <div className="page cart-page">
            <div className='title'>
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