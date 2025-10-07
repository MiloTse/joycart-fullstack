import './style.scss';
import 'swiper/css';
import type {ResponseType} from "./types";
import React, {useEffect,  useState} from "react";
import useRequest from "../../utils/useRequest";
import Banner from "./components/Banner";
import Category from "./components/Category";
import Card from "./components/Card";
import NavBar from "../../components/NavBar/NavBar";
import {API_ENDPOINTS} from "../../config/api";
import {STORAGE_LOCATION, SUCCESS_CODE, RESPONSE_DATA} from "../../constants/apiConstants";





//defaultRequestData - 使用真实的后端API
const defaultRequestData = {
    url: API_ENDPOINTS.HOME,
    method: 'GET',
}

const Home =() => {
    const localLocation= localStorage.getItem(STORAGE_LOCATION);
    const locationHistory= localLocation ? JSON.parse(localLocation) : null;
    // 位置信息功能暂时禁用,改为GET请求, 解决停用Charles Proxy的404请求报错问题。
    // if(locationHistory) {
    //     defaultRequestData.data.latitude = locationHistory.latitude;
    //     defaultRequestData.data.longitude = locationHistory.longitude;
    // }



    const [requestData, setRequestData] = useState(defaultRequestData);
    //data： 请求发送返回的结果
    // 启用自动请求，调用真实的后端API
    const {data} = useRequest<ResponseType>(requestData);

    console.log('Home API Response:', data);

    //request backend server when requestData changed
/* useRequest 已经封装了，这里不需要重新写请求
    useEffect(() => {
        request().then((data)=>{
            console.log(data);
        }).catch(e=>{
            console.log(e?.message);

            message(e?.message, 1500);
        })
    }, [requestData, request ]);

*/

    //obtain user location
    useEffect(()=>{
        //get user location if locationHistory not exist, else used the locationHistory
         if(navigator.geolocation && !locationHistory){
             navigator.geolocation.getCurrentPosition((position)=>{
                 console.log(position);
                 const { coords } = position;
                 const { latitude, longitude } = coords;
                 console.log(latitude,longitude);
                 //store the location to localStorage
                 localStorage.setItem(STORAGE_LOCATION, JSON.stringify({
                     latitude, longitude
                 }));
                 // 位置信息功能暂时禁用（因为改为GET请求）
                 // const newRequestData = {
                 //     ...defaultRequestData,
                 //     data: {
                 //         latitude, longitude
                 // }};
                 // setRequestData(newRequestData);

             },(error)=>{
                 console.log(error);
             },{
                 timeout: 500,
             })
         }
    },[locationHistory] );//when locationHistory has changed, send request again



    let  location, banners, categories, fresh  = undefined;
    const dataResult = data?.code === SUCCESS_CODE ? data[RESPONSE_DATA] : null;
    //assign value if dataResult exist
    if(dataResult) {
        //优先使用用户选择的商店位置，如果没有则使用后端API返回的位置
        if (locationHistory && locationHistory.name) {
            //用户从Nearby页面选择了商店
            location = {
                id: locationHistory.id,
                address: locationHistory.name // 显示商店名称
            };
        } else {
            //使用后端API返回的默认位置
            location = dataResult.location;
        }
        
        banners = dataResult.banners;
        categories = dataResult.categories;
        fresh = dataResult.fresh;
    }


    return (
        <div className="page home-page">
            <Banner location={location} banners={banners}/>
            <Category categories={categories}/>
            <Card title="New Product" list={fresh}/>
            <Card title="Flash Sale" list={fresh}/>
            <div className="bottom">
             -- I am the bottom line --
            </div>
            <NavBar activeName='home' />
        </div>
    )
}

export default Home;
