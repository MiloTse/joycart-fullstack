import './style.scss';
import React, { useEffect, useRef} from 'react';
import {useNavigate} from "react-router-dom";

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

    //simple way to handle click event without useCallback hook without buffering
    function handleIconClick() {
        if(localStorage.getItem('token')) {
            navigate('/home');
        }else {
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
