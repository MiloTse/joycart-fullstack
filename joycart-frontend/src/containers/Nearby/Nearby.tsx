import './style.scss';
import useRequest from "../../utils/useRequest";
import {useLocation, useNavigate} from "react-router-dom";
import React, {useState, useMemo} from "react";
import type {ResponseType} from "./types";
import {API_ENDPOINTS} from "../../config/api";

const defaultRequestData = {
    url: API_ENDPOINTS.NEARBY_STORES,
    method: 'GET',
    params: {}, // 可以在这里添加位置参数
    // The original POST request code was implemented using Charles Proxy, and it is currently commented out to facilitate future conversion into a full-stack project.
    // method: 'POST',
    // data: {
    //     latitude: 37.7304167,
    //     longitude: -122.384425,
    // }
}

const Nearby = () => {
    // 使用useMemo缓存locationHistory，避免重复解析
    // 步骤1：缓存位置历史数据
    const locationHistory = useMemo(() => {
        const localLocation = localStorage.getItem('location');
        return localLocation ? JSON.parse(localLocation) : null;
    }, []); // 空依赖数组 = 只在组件挂载时执行一次

    // 步骤2：缓存请求配置对象
    // 使用useMemo避免requestData在每次渲染时重新创建，防止无限循环
    //useMemo: cache calculate result, re-cal only when dependency changed.
    const requestData = useMemo(() => ({
        ...defaultRequestData,
        params: locationHistory ? {
            latitude: locationHistory.latitude,
            longitude: locationHistory.longitude
        } : {}
    }), [locationHistory]);// 只有locationHistory变化时才重新创建

    const { data } = useRequest<ResponseType>(requestData);
    const navigate = useNavigate();
    const [keyword, setKeyword] = useState('');

    console.log('Nearby - data:', data);
    console.log('Nearby - data?.data:', data?.data);

    // use the search keyword to filter the list
    const list = (data?.data || []).filter(
        item => item.name.toLowerCase().includes(keyword.toLowerCase())
    );

    function handleItemClick(latitude: string, longitude: string) {
        localStorage.setItem('location', JSON.stringify({
            latitude, longitude
        }));
        navigate('/home');
    }
    const handleGoBackClick = () => {
        navigate('/home');
    };
    return (
        <div className='page nearby-page'>
            <div className="title">
                <div className="iconfont title-icon"
                     onClick={handleGoBackClick} style={{ cursor: 'pointer' }}>
                    &#xe6a9;</div>
                Switch Store
            </div>
            <div className="search">
                <span className="search-icon iconfont">&#xe600;</span>
                <input 
                    className="search-input"
                    placeholder="Please enter address"
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                />
            </div>
            <div className="subtitle">Nearby Store</div>
            <ul className="store-list">
                {list.map(item => (
                    <li
                        key={item.id} 
                        className="store-list-item"
                        onClick={() => handleItemClick(item.latitude, item.longitude)}
                    >
                        <div className="store-list-item-title">{item.name}</div>
                        <div className="store-list-item-desc">
                            <span>Tel：{item.phone}</span>
                        </div>
                        <div className="store-list-item-address">{item.address}</div>
                        <div className="store-list-item-right">
                            <span className="iconfont">&#xe68e;</span>
                            {item.distance}
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Nearby;