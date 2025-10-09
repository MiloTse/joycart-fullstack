import './style.scss';
import React, {useMemo} from "react";
import NavBar from "../../components/NavBar/NavBar";
import useRequest from "../../utils/useRequest";
import {API_ENDPOINTS} from "../../config/api";
import type {ResponseType} from "./types";
import {useNavigate} from "react-router-dom";
import {message} from "../../utils/message";
import {
    RESPONSE_CODE,
    RESPONSE_DATA,
    USER_NICKNAME,
    USER_AVATAR,
    USER_VIP_LEVEL,
    USER_COUPONS,
    USER_REWARD_POINTS,
    STORAGE_TOKEN,
    STORAGE_LOCATION,
    ROUTE_LOGIN,
    DEFAULT_SHOPPER_NICKNAME,
    DEFAULT_AVATAR,
    SUCCESS_CODE,
    ERROR_MESSAGES,
    HTTP_METHODS
} from "../../constants/apiConstants";



function Profile() {
    const navigate = useNavigate();
    
    // 使用useMemo缓存请求配置对象，防止无限循环
    const requestConfig = useMemo(() => ({
        url: API_ENDPOINTS.USER_PROFILE,
        method: HTTP_METHODS.GET
    }), []); // 空依赖数组 = 只创建一次
    
    const { data, error, loaded } = useRequest<ResponseType>(requestConfig);

    console.log('=== Profile API Response ===');
    console.log('Profile - data:', data);
    console.log('Profile - error:', error);
    console.log('Profile - loaded:', loaded);
    console.log('Profile - token:', localStorage.getItem(STORAGE_TOKEN));
    
    if (data) {
        console.log('User profile data:', data[RESPONSE_DATA]);
    }
    console.log('============================');

    // 从API响应中提取用户数据，检查ResponseDTO格式
    const userData = (data?.[RESPONSE_CODE] === SUCCESS_CODE && data?.[RESPONSE_DATA]) ? data[RESPONSE_DATA] : {
        [USER_NICKNAME]: DEFAULT_SHOPPER_NICKNAME,
        [USER_AVATAR]: DEFAULT_AVATAR,
        [USER_VIP_LEVEL]: 'VIP0',
        [USER_COUPONS]: 0,
        [USER_REWARD_POINTS]: 0
    };

    function handleMemberClick() {
        console.log('member centre')
    }


    function handleLogout() {
        try {
            localStorage.removeItem(STORAGE_TOKEN);
            localStorage.removeItem(STORAGE_LOCATION);
            message(ERROR_MESSAGES.LOGOUT_SUCCESS);
            //jump to login page
            navigate(ROUTE_LOGIN);
        } catch (error) {
            console.error('Logout error:', error);
            message(ERROR_MESSAGES.LOGOUT_FAILED);
        }
    }

    return (
        <div className="profile-page">
            {/*title section*/}
            <div className='profile-title'>
                profile
            </div>
            {/*profile information section*/}
            <div className='profile-profile'>
                <div className='profile-profile-left'>
                    <img className='avatar' alt='avatar' src={userData.avatar}/>
                    <div className='user-info'>
                        <div className='nickname'>{userData.nickname}</div>
                        <div className='logout-button' onClick={handleLogout}>
                            Logout
                        </div>
                    </div>
                    <div className='vip-info'>
                        <span  className='vip-level'>{userData.vipLevel}</span>
                    </div>
                </div>
                <div className='profile-profile-right'>
                    <button className='profile-profile-right member-centre' onClick={handleMemberClick}>
                        Member
                    </button>
                </div>
            </div>

            {/* point section */}
            <div className="profile-points">
                <div className="profile-points-item">
                    <div className="profile-points-value">{userData.coupons}</div>
                    <div className="profile-points-label">Coupons</div>
                </div>
                <div className="profile-points-item">
                    <div className="profile-points-value">{userData.rewardPoints}</div>
                    <div className="profile-points-label">Reward Points</div>
                </div>
                <div className="profile-points-item">

                </div>
            </div>
            {/* feature section */}
            <div className="profile-features">
                <div className="profile-features-row">
                    <div className="profile-feature-item">
                        <div className="profile-feature-icon iconfont">&#xe603;</div>
                        <div className="profile-feature-text">All Orders</div>
                    </div>
                    <div className="profile-feature-item">
                        <div className="profile-feature-icon iconfont">&#xe612;</div>
                        <div className="profile-feature-text">Pending Payment</div>
                    </div>
                    <div className="profile-feature-item">
                        <div className="profile-feature-icon iconfont">&#xe63f;</div>
                        <div className="profile-feature-text">Awaiting Shipment</div>
                    </div>
                    <div className="profile-feature-item">
                        <div className="profile-feature-icon iconfont">&#xe6b1;</div>
                        <div className="profile-feature-text">Awaiting Delivery</div>
                    </div>
                </div>
 
                <div className="profile-features-row">
                    <div className="profile-feature-item">
                        <div className="profile-feature-icon iconfont">&#xe611;</div>
                        <div className="profile-feature-text">Returns & Refunds</div>
                    </div>
                    <div className="profile-feature-item">
                        <div className="profile-feature-icon iconfont">&#xec2e;</div>
                        <div className="profile-feature-text">Customer Service</div>
                    </div>
                    <div className="profile-feature-item">
                        <div className="profile-feature-icon iconfont">&#xe643;</div>
                        <div className="profile-feature-text">Settings</div>
                    </div>
                    <div className="profile-feature-item">
                        <div className="profile-feature-icon iconfont">&#xe640;</div>
                        <div className="profile-feature-text">Address</div>
                    </div>
                </div>
            </div>
            
            <NavBar activeName='profile'/>
        </div>
    )

}

export default Profile;