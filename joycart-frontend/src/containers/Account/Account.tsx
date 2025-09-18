import './style.scss'
import React, {useEffect} from "react";
import {Link, Outlet, useLocation} from "react-router-dom";
import {useNavigate} from "react-router-dom";


const Account = ()=> {
    const navigate = useNavigate();
    const location = useLocation();
    console.log(location);
    const isLoginActivated = location.pathname === '/account/login';
    const loginActiveClass = isLoginActivated ? 'tab-item-active' : '';
    const registerActiveClass = !isLoginActivated ? 'tab-item-active' : '';


    //if login success, redirect to home page
    useEffect(() => {
        if(localStorage.getItem('token')) {
            navigate('/home');
        }
    }, [navigate]);

    return (
        <div className="page account-page">
            <div className="tab">
                <div className={`tab-item tab-item-left ${loginActiveClass}`}>
                    <Link to="/account/login">login</Link>
                </div>
                <div className={`tab-item tab-item-right ${registerActiveClass}`}>
                    <Link to="/account/register">register</Link>
                </div>
            </div>
            <Outlet />
        </div>


)

}

export default Account