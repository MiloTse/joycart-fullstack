import './style.scss';
import React from "react";
import NavBar from "../../components/NavBar/NavBar";
import useRequest from "../../utils/useRequest";
import {API_ENDPOINTS} from "../../config/api";
import type {ResponseType} from "./types";



function Profile() {
    // 获取用户资料数据
    const { data } = useRequest<ResponseType>({
        url: API_ENDPOINTS.USER_PROFILE,
        method: 'GET'
    });

    // 从API响应中提取用户数据，如果没有数据则使用默认值
    const userData = data?.data || {
        nickname: 'Shopper',
        avatar: '/images/external/category-list-5.png',
        vipLevel: 'VIP0',
        coupons: 0,
        rewardPoints: 0
    };

    function handleMemberClick() {
        console.log('member centre')
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
                    <div className='nickname'>{userData.nickname}</div>
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