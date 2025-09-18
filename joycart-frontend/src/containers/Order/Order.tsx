import './style.scss';
import React, {useEffect, useState} from "react";
import useRequest from "../../utils/useRequest";
import {ResponseType, ResponseDataType, AddressResponseType, AddressItemType, PaymentResponseType} from "./types";
import {message} from "../../utils/message";
import {useNavigate, useParams} from "react-router-dom";
import Popover from "../../components/Popover/Popover";
import { Picker } from 'antd-mobile';




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
     useEffect(() => {
         request({
             url: '/orderDetail.json',
             method: 'GET',
             params: {id: params.id}
         }).then((response)=>{
             setData(response.data);
             console.log(response. data);
         }).catch((e)=>{
             message(e.message);
         })
     }, [params,request]);






     function handleReceiverClick() {
           setShowAddress(true);
           addressRequest({
               url: '/userAddress.json',
               method: 'GET'
           }).then((response)=>{
               console.log(response.data);
                 setAddressList(response.data);
           }).catch((e)=>{
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
         const orderId =params.id;
         const addressId =data?.address.id;
         const time =data?.time;
         paymentRequest({
             method:'GET',
             url:'/pay.json',
             // The original POST request code was implemented using Charles Proxy, and it is currently commented out to facilitate future conversion into a full-stack project.
             // method:'POST',
             // data:{
             //     orderId,
             //     addressId,
             //     time,
             //     payWay
             // }
         }).then((response)=>{
             if(response.data){

                 navigate('/home');
             }else{
                 message('Payment failed');
             }
             console.log(response);
         }).catch((e)=>{
             message(e.message);
         });
     }

     return data? (
        <div className="page order-page">
            <div className='title'>
                <div className="iconfont" onClick={() => {
                    navigate(-1)
                }}>&#xe6a9;</div>
                <div className="text">Confirm Order</div>
            </div>
            <div className='receiver' onClick={handleReceiverClick}>
                <div className='iconfont'>&#xe68e; </div>
                <div className='receiver-content'>
                    <div className='receiver-name'>
                        Receiver: {data.address.name}
                        <span className='receiver-phone'>{data.address.phone}</span>
                    </div>
                    <div className='receiver-address'>
                        Address: {data.address.address}
                    </div>
                </div>

            </div>
            <div className='delivery'>
                <div className='delivery-text'>Delivery Time</div>
                <div className='delivery-select' onClick={()=>{setShowTimeRange(true)}}>
                    {data.time?.[0]} {data.time?.[1]}:{data.time?.[2]}
                </div>
            </div>

            {
                data.shop.map(shop =>  (
                    <div key={shop.shopId}>
                        {/*iterate show div from dynamic data*/}
                        <div className='shop'>
                            <div className='shop-title'>
                                <span className='iconfont'>&#xe7ce;</span>
                                {shop.shopName}
                            </div>
                            <div className='shop-products'>

                                {
                                    shop.cartList.map(product => (
                                        <div className='shop-product' key={product.productId}>
                                            <img src={product.imgUrl}
                                                 alt={product.title}
                                                 className='shop-product-img'/>
                                            <div className='shop-product-content'>
                                                <div className='shop-product-title'>
                                                    {product.title}
                                                </div>
                                                <div className='shop-product-kilo'>{product.weight}</div>
                                            </div>
                                            <div className='shop-product-order'>
                                                <div>&#36;{product.price}</div>
                                                <div>X{product.count}</div>
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
                    Total:
                    <span className='footer-total-price'>
                        <span className='footer-total-symbol'>&#36;</span>
                        {data.total}
                    </span>
                </div>
                <div className='footer-submit' onClick={()=>{setShowPayment(true)}}>Place Order</div>
            </div>
            <Popover show={showAddress} blankClickCallBack={()=> setShowAddress(false)}>
                <div className='address-popover'>
                    <div className='address-popover-title'>Choose Address</div>
                    {
                        addressList.map((address ) => (
                            <div className='address-item'
                                 key={address.id}
                                 onClick={()=>
                                handleAddressClick(address)}
                            >
                                <div className='address-item-content'>
                                    <div className='address-item-name'>
                                        Receiver: {address.name}
                                        <span className='address-item-phone'>{address.phone}</span>
                                    </div>
                                    <div className='address-item-address'>
                                        Address: {address.address}
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
                    <div className='payment-popover-title'>Choose Payment</div>
                    <div className='payment-popover-price'>&#36; {data.total}</div>
                    <div className='payment-popover-products'>
                        <div className='payment-popover-product' onClick={()=>{setPayWay('wechat')}}>
                            <img className='payment-popover-img' src='/images/external/weixin.png' alt='wechat' />
                            WeChat
                            <div className={payWay === 'wechat' ? 'radio radio-active' : 'radio'}></div>

                        </div>
                        <div className='payment-popover-product' onClick={()=>{setPayWay('cash')}}>
                            <img className='payment-popover-img' src='/images/external/cash.png' alt='weixin' />
                            Balance({data.balance})
                            <div className={payWay === 'cash' ? 'radio radio-active' : 'radio'}></div>
                        </div>
                    </div>
                    <div className='payment-popover-button' onClick={handleOrderSubmit} >
                        Pay Now
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