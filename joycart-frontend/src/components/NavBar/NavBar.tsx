import React from "react";
import './style.scss';
import {useNavigate} from "react-router-dom";

const items = [
    {
        name: 'Home',
        text: 'Home',
        icon: '&#xe608;',
        path: '/home'
    },
    {
        name: 'Category',
        text: 'Category',
        icon: '&#xe609;',
        path: '/category'
    },
    {
        name: 'Cart',
        text: 'Cart',
        icon: '&#xe601;',
        path: '/cart',
    },
    {
        name: 'Profile',
        text: 'Profile',
        icon: '&#xe602;',
        path: '/profile',
    }
]


function NavBar   (props: {activeName:string})   {
    const navigate = useNavigate();
    const {activeName } = props;


    return (
            <div className="navbar">
                {
                    items.map(  item => (
                            <button className= { activeName.toLowerCase() === item.name.toLowerCase() ?
                                'navbar-item navbar-item-active' : 'navbar-item' }
                                 onClick={() => navigate(item.path)}
                                 key={item.name}
                                 aria-label={item.text}
                                 aria-current={activeName.toLowerCase() === item.name.toLowerCase() ? 'page' : undefined}
                            >
                                <p className="iconfont" dangerouslySetInnerHTML={{__html: item.icon}}/>
                                <p className="navbar-item-title">{item.text}</p>
                            </button>

                        )
                    )
                }
            </div>
    );
}

export default NavBar;