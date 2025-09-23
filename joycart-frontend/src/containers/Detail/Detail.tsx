
import './style.scss';
import React, {useEffect, useRef, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import useRequest from "../../utils/useRequest";
import type {ResponseType, CartResponseType, AddToCartResponseType} from "./types";
import Popover from "../../components/Popover/Popover";
import {message} from "../../utils/message";
import {CartChangeResponseType} from "../../types";
import {API_ENDPOINTS} from "../../config/api";


const Detail = () => {
    const navigate = useNavigate();
    //showCart default false
    const [showCart, setShowCart] = useState<boolean>(false);
    const params = useParams<{id:string}>();

    //assemble request logic of Detail product
    const requestData = useRef({
        url: `${API_ENDPOINTS.PRODUCT_DETAIL}/${params?.id}`,
        method: 'GET',
    });


    //data is a dynamic data fetch from api detail.json
    const {data} = useRequest<ResponseType>(requestData.current);
    
    // 添加ResponseDTO格式的调试日志
    useEffect(() => {
        if (data) {
            console.log('=== Product Detail API Response ===');
            console.log('Full response:', data);
            console.log('Response structure:', {
                code: data.code,
                message: data.message,
                data: data.data
            });
            console.log('Product info:', data.data);
            console.log('==================================');
        }
    }, [data]);
    
    //define default value if undefined or null
    const result = data?.code === 200 ? data.data : {
        id: '',
        imgUrl: '',
        title: '',
        subtitle: '',
        price: 0,
        sales: 0,
        origin: '',
        specification: '',
        detail: ''
    };
    //shopping cart quantity that had been added to cart
    const [count, setCount] = useState(0);
    const [tempCount, setTempCount] = useState(0);
    //manually trigger a request
    const {request: cartRequest} = useRequest<CartResponseType>({manual: true});
    //manually trigger add to cart request
    const {request: addToCartRequest} = useRequest<AddToCartResponseType>({manual: true});
    useEffect(() => {
        cartRequest({
                url: API_ENDPOINTS.CART_ITEM,
                method: 'GET',
                params: {id: params!.id},
            }
         ).then(response => {
             console.log('=== Cart Item Count API Response ===');
             console.log('Full response:', response);
             console.log('Response structure:', {
                 code: response.code,
                 message: response.message,
                 data: response.data
             });
             console.log('Count value:', response.data.count);
             console.log('=====================================');
             
             // 使用新的ResponseDTO格式：response.data.count
             const count = response.data.count || 0;
             setCount(count);
             setTempCount(count);
         }).catch((e)=>{
            message(e.message);
        })
    }, [cartRequest, params]);


    function changTempCount(tempCount: number) {
        if(tempCount<0){
            setTempCount(0);
        }else{
            setTempCount(tempCount);
        }
     }

     //when close popover, set tempCount to count
     function closeMask () {
        setTempCount(count);
         setShowCart(false)
     }

     //handle the action to change cart info and send request to server
    const {request: cartChangeRequest} = useRequest<CartChangeResponseType>({manual: true});
    function changeCartInfo() {
        cartChangeRequest({
            url: API_ENDPOINTS.CART_CHANGE,
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            data: new URLSearchParams({
                id: params!.id || '',
                count: tempCount.toString()
            }).toString()
        }).then(response => {
            console.log('=== Cart Change API Response ===');
            console.log('Full response:', response);
            console.log('Response structure:', {
                code: response.code,
                message: response.message,
                data: response.data
            });
            console.log('Action:', response.data?.action);
            console.log('================================');

            if(response.code === 200){
                setCount(tempCount);
                setShowCart(false);
                // 使用后端返回的消息，如果没有则使用默认消息
                const successMessage = response.message || 'Cart updated successfully!';
                message(successMessage);
            } else {
                message(response.message || 'Cart update failed');
            }
        }).catch((e)=>{
            console.error('Cart change error:', e);
            message(e.message);
        })
    }


    function handleShoppingCartClick() {
        console.log('=== Shopping Cart clicked ===');
        console.log('Current count:', count);
        console.log('Product ID:', params.id);
        console.log('============================');

        //只有当count > 0时才调用API
        if (count > 0) {
            //调用add to cart API
            addToCartRequest({
                url: API_ENDPOINTS.CART_ADD,
                method: 'POST',
                data: `productId=${params.id}&count=${count}`,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(response => {
                console.log('=== Add to Cart API Response ===');
                console.log('Full response:', response);
                console.log('Response structure:', {
                    code: response.code,
                    message: response.message,
                    data: response.data
                });
                console.log('Action:', response.data?.action);
                console.log('================================');
                
                // 使用后端返回的消息，如果没有则使用默认消息
                const successMessage = response.message || 'Product Added To Cart';
                message(successMessage);
                //API调用成功后，重新获取购物车数量以刷新显示
                cartRequest({
                    url: API_ENDPOINTS.CART_ITEM,
                    method: 'GET',
                    params: {id: params!.id},
                }).then(countResponse => {
                    console.log('=== Cart Count Refresh Response ===');
                    console.log('Count response:', countResponse);
                    
                    const count = countResponse.data.count || 0;
                    setCount(count);
                    console.log('Updated cart count:', count);
                    console.log('===================================');
                }).catch(countError => {
                    console.error('Failed to refresh cart count:', countError);
                });
                //API调用成功后跳转
                navigate(`/cart?from=detail&productId=${params.id}`);
            }).catch(error => {
                console.error('Add to cart error:', error);
                message('添加到购物车失败，请重试');
                //即使API失败也跳转
                navigate(`/cart?from=detail&productId=${params.id}`);
            });
        } else {
            // count为0时直接跳转，不调用API
            console.log('Count is 0, navigating directly');
            navigate(`/cart?from=detail&productId=${params.id}`);
        }
    }

    return result ? (
        <div className="page detail-page">
            {/*title area */}
            <div className="title">
                <div className="iconfont" onClick={()=>{navigate(-1)}}>&#xe6a9;</div>
                <div className="text">Detail</div>
            </div>
            <img className='image' alt='' src={result.imgUrl}/>
            {/*main area */}
            <div className='main'>
                <div className='main-price'><span className="main-price-symbol">&#36;</span>
                    {result.price}</div>
                <div className='main-sales'>sold {result.sales}</div>
                 <div className='main-content'>
                    <div
                        className='main-content-title'>{result.title}
                    </div>
                    <p className='main-content-subtitle'>{result.subtitle}</p>
                </div>

            </div>
            {/*specification area */}
            <div className='spec'>
                <div className='spec-title'>specification info</div>
                <div className='spec-content'>
                    <div className='spec-content-left'>
                        <p className='spec-content-item'>origin</p>
                        <p className='spec-content-item'>spec</p>
                    </div>
                    <div className='spec-content-right'>
                        <p className='spec-content-item'>{result.origin}</p>
                        <p className='spec-content-item'>{result.specification}</p>
                    </div>
                </div>
            </div>
            {/*product detail area */}
            <div className='detail'>
                <div className='detail-title'>product detail</div>
                <div className='detail-content'>
                    {result.detail}

                </div>
            </div>
            {/*docker area */}
            <div className='docker'>
                <div className='cart-icon'
                     onClick={handleShoppingCartClick}
                     style={{ cursor: 'pointer' }}>
                    <div className='iconfont'>
                        &#xe949;
                        <span className='icon-count'>{count}</span>
                    </div>
                    <div className='icon-text'>
                      Shopping Cart
                    </div>
                </div>

                <div className='cart-button' onClick={()=>{setShowCart(true)}}>Add to Cart</div>
            </div>
            {/*Popover area*/}
            <Popover show={showCart} blankClickCallBack={ closeMask}>
                <div className='cart'>
                    <div className='cart-content'>
                        <img className='cart-content-img' alt='' src={result.imgUrl}/>
                        <div className='cart-content-info'>
                            <div className='cart-content-title'>{result.title}</div>
                            <div className='cart-content-price'>
                                <span className='cart-content-price-symbol'>&#36;</span>
                                {result.price}
                            </div>
                         </div>
                    </div>
                    <div className='cart-count'>
                        <div className='cart-count-content'>
                            Quantity:
                            <div className='cart-count-counter'>
                                 <div className='cart-count-button'
                                      onClick={()=>{changTempCount(tempCount-1)}}>
                                     -
                                 </div>
                                <div className='cart-count-text'>{tempCount}</div>
                                <div className='cart-count-button'
                                     onClick={()=>{changTempCount(tempCount+1)}}>
                                    +
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className='cart-button' onClick={changeCartInfo}>Add to Cart</div>
                </div>
            </Popover>
        </div>
    ) : null;

}

export default Detail;