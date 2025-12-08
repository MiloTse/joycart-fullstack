import React from "react";
import './style.scss';
import {useNavigate} from "react-router-dom";
import useLanguage from "../../hooks/useLanguage";
import {translate, UI_TRANSLATION_KEYS} from "../../utils/i18n";

const items = [
    {
        name: 'Home',
        icon: '&#xe608;',
        path: '/home',
        translationKey: UI_TRANSLATION_KEYS.nav.home
    },
    {
        name: 'Category',
        icon: '&#xe609;',
        path: '/category',
        translationKey: UI_TRANSLATION_KEYS.nav.category
    },
    {
        name: 'Cart',
        icon: '&#xe601;',
        path: '/cart',
        translationKey: UI_TRANSLATION_KEYS.nav.cart
    },
    {
        name: 'Profile',
        icon: '&#xe602;',
        path: '/profile',
        translationKey: UI_TRANSLATION_KEYS.nav.profile
    }
];


function NavBar   (props: {activeName:string})   {
    const navigate = useNavigate();
    const {activeName } = props;
    const language = useLanguage();


    return (
            <div className="navbar">
                {
                    items.map(  item => {
                        const label = translate(item.translationKey, language);
                        return (
                            <button className= { activeName.toLowerCase() === item.name.toLowerCase() ?
                                'navbar-item navbar-item-active' : 'navbar-item' }
                                 onClick={() => navigate(item.path)}
                                 key={item.name}
                                 aria-label={label}
                                 aria-current={activeName.toLowerCase() === item.name.toLowerCase() ? 'page' : undefined}
                            >
                                <p className="iconfont" dangerouslySetInnerHTML={{__html: item.icon}}/>
                                <p className="navbar-item-title">{label}</p>
                            </button>
                        );
                    })
                }
            </div>
    );
}

export default NavBar;