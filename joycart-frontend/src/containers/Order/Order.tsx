import './style.scss';
import React, {useEffect, useState} from "react";
import useRequest from "../../utils/useRequest";
import {ResponseType, ResponseDataType, AddressResponseType, AddressItemType, PaymentResponseType} from "./types";
import {message} from "../../utils/message";
import {useNavigate, useParams} from "react-router-dom";
import Popover from "../../components/Popover/Popover";
import { Picker } from 'antd-mobile';
import {API_ENDPOINTS} from "../../config/api";
import useLanguage from "../../hooks/useLanguage";
import {translate, UI_TRANSLATION_KEYS} from "../../utils/i18n";




 function Order() {
     const {request } = useRequest<ResponseType>({ manual:true})
     const {request: addressRequest } = useRequest<AddressResponseType>({ manual:true})
     //store order data from requesting
     const [data, setData] = useState<ResponseDataType | null >(null);
     const params = useParams<{id:string}>()
     const [showAddress, setShowAddress] = useState(false);
     const [addressList, setAddressList] = useState<AddressItemType[]>([]);
     const [showTimeRange, setShowTimeRange] = useState(false);
     const [showPayment, setShowPayment] = useState(false);
     const [payWay, setPayWay] = useState('wechat');
     const {request: paymentRequest } = useRequest<PaymentResponseType>({ manual:true})
     const navigate = useNavigate();
     const language = useLanguage();
     useEffect(() => {
         request({
             url: API_ENDPOINTS.ORDER_DETAIL,
             method: 'GET',
             params: {id: params.id}
         }).then((response)=>{
             console.log('=== Order Detail API Response ===');
             console.log('Full response:', response);
             console.log('Order detail data:', response.data);
             console.log('================================');
             
             if(response.code === 200) {
                 setData(response.data);
             } else {
                 message(response.message || translate(UI_TRANSLATION_KEYS.order.detailFailed, language));
             }
         }).catch((e)=>{
             console.error('Order detail error:', e);
             message(e.message);
         })
     }, [params,request]);






     function handleReceiverClick() {
           setShowAddress(true);
           addressRequest({
               url: API_ENDPOINTS.ORDER_ADDRESSES,
               method: 'GET'
           }).then((response)=>{
               console.log('=== Order Addresses API Response ===');
               console.log('Full response:', response);
               console.log('Response structure:', {
                   code: response.code,
                   message: response.message,
                   data: response.data
               });
               console.log('Address list:', response.data);
               console.log('====================================');
               
               if(response.code === 200) {
                   setAddressList(response.data);
               } else {
                   message(response.message || translate(UI_TRANSLATION_KEYS.order.addressListFailed, language));
               }
           }).catch((e)=>{
               console.error('Order addresses error:', e);
               message(e.message);
           });
     }
     console.log(addressList);


     function handleAddressClick(address: AddressItemType) {
         if(data){
             const newData = {...data};
             newData.address = address;
             setData(newData);
         }
         setShowAddress(false);

     }


     function handleOrderSubmit() {
         const orderId = params.id;
         const addressId = data?.address.id;
         const timeArray = data?.time;
         const timeString = timeArray ? timeArray.join(' ') : ''; // 将时间数组转换为字符串
         
         paymentRequest({
             method: 'POST',
             url: API_ENDPOINTS.ORDER_PAY,
             headers: {
                 'Content-Type': 'application/x-www-form-urlencoded',
             },
             data: new URLSearchParams({
                 orderId: orderId || '',
                 addressId: addressId || '',
                 time: timeString,
                 payWay: payWay
             }).toString()
         }).then((response)=>{
             console.log('=== Order Payment API Response ===');
             console.log('Full response:', response);
             console.log('Payment result:', response.data);
             console.log('==================================');
             
             if(response.code === 200 && response.data){
                 message(translate(UI_TRANSLATION_KEYS.order.paymentSuccess, language));
                 navigate('/home');
             } else {
                 message(response.message || translate(UI_TRANSLATION_KEYS.order.paymentFailed, language));
             }
         }).catch((e)=>{
             console.error('Order payment error:', e);
             message(e.message);
         });
     }

     // 添加加载状态，确保数据完全加载后再渲染
     if (!data) {
         return (
             <div className="order-loading">
                 {translate(UI_TRANSLATION_KEYS.order.loading, language)}
             </div>
         );
     }

     return data ? (
        <div className="page order-page">
            <div className='title'>
                <div className="iconfont" onClick={() => {
                    navigate(-1)
                }}>&#xe6a9;</div>
                <div className="text">{translate(UI_TRANSLATION_KEYS.order.title, language)}</div>
            </div>
            <div className='receiver' onClick={handleReceiverClick}>
                <div className='iconfont'>&#xe68e; </div>
                <div className='receiver-content'>
                    <div className='receiver-name'>
                        {translate(UI_TRANSLATION_KEYS.order.receiverLabel, language)} {data?.address?.name}
                        <span className='receiver-phone'>{data?.address?.phone}</span>
                    </div>
                    <div className='receiver-address'>
                        {translate(UI_TRANSLATION_KEYS.order.addressLabel, language)} {data?.address?.address}
                    </div>
                </div>

            </div>
            <div className='delivery'>
                <div className='delivery-text'>{translate(UI_TRANSLATION_KEYS.order.deliveryTime, language)}</div>
                <div className='delivery-select' onClick={()=>{setShowTimeRange(true)}}>
                    {data.time?.[0]} {data.time?.[1]}:{data.time?.[2]}
                </div>
            </div>

            {
                data?.shop?.map(shop =>  (
                    <div key={shop?.shopId}>
                        {/*iterate show div from dynamic data*/}
                        <div className='shop'>
                            <div className='shop-title'>
                                <span className='iconfont'>&#xe7ce;</span>
                                {shop?.shopName}
                            </div>
                            <div className='shop-products'>

                                {
                                    shop.cartList?.map(product => (
                                        <div className='shop-product' key={product?.productId}>
                                            <img src={product?.imgUrl}
                                                 alt={product?.title}
                                                 className='shop-product-img'/>
                                            <div className='shop-product-content'>
                                                <div className='shop-product-title'>
                                                    {product?.title}
                                                </div>
                                                <div className='shop-product-kilo'>{product?.weight}</div>
                                            </div>
                                            <div className='shop-product-order'>
                                                <div>&#36;{product?.price}</div>
                                                <div>X{product?.count}</div>
                                            </div>
                                        </div>
                                    ))
                                }

                            </div>
                        </div>


                    </div>
                ))
            }


            <div className='footer'>
                <div className='footer-total'>
                    {translate(UI_TRANSLATION_KEYS.order.totalLabel, language)}
                    <span className='footer-total-price'>
                        <span className='footer-total-symbol'>&#36;</span>
                        {data?.total}
                    </span>
                </div>
                <div className='footer-submit' onClick={()=>{setShowPayment(true)}}>{translate(UI_TRANSLATION_KEYS.order.placeOrderButton, language)}</div>
            </div>
            <Popover show={showAddress} blankClickCallBack={()=> setShowAddress(false)}>
                <div className='address-popover'>
                    <div className='address-popover-title'>{translate(UI_TRANSLATION_KEYS.order.chooseAddressTitle, language)}</div>
                    {
                        addressList.map((address ) => (
                            <div className='address-item'
                                 key={address.id}
                                 onClick={()=>
                                handleAddressClick(address)}
                            >
                                <div className='address-item-content'>
                                    <div className='address-item-name'>
                                        {translate(UI_TRANSLATION_KEYS.order.receiverLabel, language)} {address.name}
                                        <span className='address-item-phone'>{address.phone}</span>
                                    </div>
                                    <div className='address-item-address'>
                                        {translate(UI_TRANSLATION_KEYS.order.addressLabel, language)} {address.address}
                                    </div>
                                </div>

                            </div>
                        ))
                    }





                </div>

            </Popover>
            {/*popover for payment method to select*/}
            <Popover show={showPayment} blankClickCallBack={()=> setShowPayment(false)}>
                <div className='payment-popover'>
                    <div className='payment-popover-title'>{translate(UI_TRANSLATION_KEYS.order.choosePaymentTitle, language)}</div>
                    <div className='payment-popover-price'>&#36; {data?.total}</div>
                    <div className='payment-popover-products'>
                        <div className='payment-popover-product' onClick={()=>{setPayWay('wechat')}}>
                            <img className='payment-popover-img' src='/images/external/weixin.png' alt='wechat' />
                            {translate(UI_TRANSLATION_KEYS.order.paymentWeChat, language)}
                            <div className={payWay === 'wechat' ? 'radio radio-active' : 'radio'}></div>

                        </div>
                        <div className='payment-popover-product' onClick={()=>{setPayWay('cash')}}>
                            <img className='payment-popover-img' src='/images/external/cash.png' alt='weixin' />
                            {translate(UI_TRANSLATION_KEYS.order.paymentBalance, language)}({data?.balance})
                            <div className={payWay === 'cash' ? 'radio radio-active' : 'radio'}></div>
                        </div>
                    </div>
                    <div className='payment-popover-button' onClick={handleOrderSubmit} >
                        {translate(UI_TRANSLATION_KEYS.order.payNowButton, language)}
                    </div>
                </div>
            </Popover>
            <Picker
                columns={data.timeRange || []}
                visible={showTimeRange}
                onClose={() => {
                    setShowTimeRange(false);
                }}
                value={data?.time}
                onConfirm={v => {
                    // setValue(v)
                    if (data){
                        const newData = {...data};
                        newData.time = v as string[];
                        setData(newData);
                    }
                    console.log(v);
                    setShowTimeRange(false);
                }}
            />
        </div>
     ) : null;
 }


export default Order;