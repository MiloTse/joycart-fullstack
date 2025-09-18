
import './style.scss';
import React, {useEffect, useRef, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import useRequest from "../../utils/useRequest";
import type {ResponseType, CartResponseType} from "./types";
import Popover from "../../components/Popover/Popover";
import {message} from "../../utils/message";
import {CartChangeResponseType} from "../../types";


const Detail = () => {
    const navigate = useNavigate();
    //showCart default false
    const [showCart, setShowCart] = useState<boolean>(false);
    const params = useParams<{id:string}>();

    //assemble request logic of Detail product
    const requestData = useRef({
        url: '/detail.json',
        method: 'GET',
        params: { id: params?.id},
    });


    //data is a dynamic data fetch from api detail.json
    const {data} = useRequest<ResponseType>(requestData.current);
    //define default value if undefined or null
    const result = data?.data || {
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
    useEffect(() => {
        cartRequest({
                url: '/cart.json',
                method: 'GET',
                params: {id: params!.id},
            }
         ).then(response => {
             setCount(response.data.count);
             //when request success, set tempCount to count as well
             setTempCount(response.data.count);
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
            url: '/cartChange.json',
            method: 'GET',
            params: {id: params!.id, count: tempCount},
        }).then(response => {
            if(response.success){
                setCount(tempCount);
                setShowCart(false);
            }
        }).catch((e)=>{
            message(e.message);
        })
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
                <div className='cart-icon'>
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