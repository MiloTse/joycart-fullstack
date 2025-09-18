import './style.scss';
import React from "react";
import NavBar from "../../components/NavBar/NavBar";



function Profile() {
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
                    <img className='avatar' alt='avatar' src='/images/external/category-list-5.png'/>
                    <div className='nickname'>Tom Wang</div>
                    <div className='vip-info'>
                        <span  className='vip-level'>VIP5</span>
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
                    <div className="profile-points-value">4</div>
                    <div className="profile-points-label">Coupons</div>
                </div>
                <div className="profile-points-item">
                    <div className="profile-points-value">258</div>
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