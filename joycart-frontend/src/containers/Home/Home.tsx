import './style.scss';
import 'swiper/css';
import type {ResponseType} from "./types";
import React, {useEffect,  useState} from "react";
import useRequest from "../../utils/useRequest";
import Banner from "./components/Banner";
import Category from "./components/Category";
import Card from "./components/Card";
import NavBar from "../../components/NavBar/NavBar";
//import { message } from "../../utils/message";





//defaultRequestData
const defaultRequestData = {
    url: '/home.json',
    method: 'GET',
    // The original POST request code was implemented using Charles Proxy, and it is currently commented out to facilitate future conversion into a full-stack project.
    // method: 'POST',
    // data: {
    //     //default value
    //     latitude:  45.3497856,
    //     longitude: -75.7554394,
    // }
}

const Home =() => {
    const localLocation= localStorage.getItem('location');
    const locationHistory= localLocation ? JSON.parse(localLocation) : null;
    // 位置信息功能暂时禁用,改为GET请求, 解决停用Charles Proxy的404请求报错问题。
    // if(locationHistory) {
    //     defaultRequestData.data.latitude = locationHistory.latitude;
    //     defaultRequestData.data.longitude = locationHistory.longitude;
    // }



    const [requestData, setRequestData] = useState(defaultRequestData);
    //data： 请求发送返回的结果
    const {data} = useRequest<ResponseType>(requestData);

    console.log(data);

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
                 localStorage.setItem('location', JSON.stringify({
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
    const dataResult = data?.data;
    //assign value if dataResult exist
    if(dataResult) {
        location = dataResult.location;
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
