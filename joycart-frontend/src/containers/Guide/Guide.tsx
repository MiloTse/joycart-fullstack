import './style.scss';
import React, { useEffect, useRef} from 'react';
import {useNavigate} from "react-router-dom";
import useRequest from "../../utils/useRequest";

//define a custom hook to handle animation
const useRefAnimation = ()=> {
    //处理动画相关的逻辑
    //handle the animation related logic
    const ref = useRef<HTMLDivElement>(null!);
    useEffect(()=>{
        ref.current.style.opacity = '1';
    },[]);
    return ref;
}



const Guide =() => {
    //处理动画相关的逻辑
    //handle the animation related logic
/*    const ref = useRef<HTMLDivElement>(null!);
    useEffect(()=>{
        ref.current.style.opacity = '1';
    })*/
    //use custom hook to handle animation
    const ref = useRefAnimation();

    //处理页面跳转相关的逻辑
    //handle the page jump related logic
    const navigate = useNavigate();
    //use useCallback hook as it will not be changed frequently
    //useCallback hook will only be called once when the component is mounted
/*
    const handleIconClick = useCallback(()=>{
        navigate('/login');
    },[navigate]);
*/

    // 使用useRequest来验证token有效性
    // Use useRequest to validate token validity
    const {request: validateToken} = useRequest({manual: true});

    //simple way to handle click event without useCallback hook without buffering
    async function handleIconClick() {
        const token = localStorage.getItem('token');
        
        if(!token) {
            // 没有token，直接跳转到登录页面
            // No token, redirect to login page directly
            navigate('/account/login');
            return;
        }

        try {
            // 有token，这里需要验证其有效性 - 尝试调用需要认证的API
            // Has token, here should validate its validity - try to call an authenticated API
            await validateToken({
                url: '/home',
                method: 'GET'
            });
            
            // token有效，跳转到home页面
            // Token is valid, redirect to home page
            navigate('/home');
        } catch (error) {
            console.log("invalid token:", token);
            // token无效或验证失败，清除token并跳转到登录页面
            // Token is invalid or validation failed, clear token and redirect to login page
            localStorage.removeItem('token');
            navigate('/account/login');
        }
    }


    return (
        <div ref={ref} className="page guide-page">
            <img alt="JoyCart"
                 className="main-pic"
                 src={require('../../images/halg_logo_icon_@2x.png')}
            />
            <p className="title">Happy Shopping</p>
            <img alt="Shop with Joy, Eat with Trust"
                 className="sub-pic"
                 src={require('../../images/slogan_word_icon_@2x.png')}
            />
            <div className="iconfont arrow-icon"
            onClick={handleIconClick}
            >&#xe60c;</div>
        </div>
    )
}

export default Guide;
