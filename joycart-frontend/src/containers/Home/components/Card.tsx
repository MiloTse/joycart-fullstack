import React from "react";
import { CardListType} from "../types";
import {useNavigate} from "react-router-dom";


type CardPropsType = {
    title: string;
    list: CardListType | undefined;
}




const Card = (props: CardPropsType) => {
    const {title, list} = props;

    const navigate = useNavigate();
    function handleItemClick(productId: string) {
        navigate(`/detail/${productId}`);
    }

    return (
        <div className="card">
            <h3 className="card-title">
                <img
                    alt={title}
                    className="card-title-img"
                    src="/images/external/hot.png"/>
                {title}
                <div className="card-title-more">
                    more
                    <span className="iconfont">&#xe70d;</span>
                </div>
            </h3>
            <div className="card-content">

                {
                    (list || []).map((item) => {
                        return (
                            <div className="card-content-item" key={item.id} onClick={() => handleItemClick(item.id)}>
                                <img
                                    alt={item.name}
                                    className="card-content-item-img"
                                    src={item.imgUrl}/>
                                <p className='card-content-item-desc'>{item.name}</p>
                                <div className='card-content-item-price'>
                                    <span className='card-content-item-price-symbol'>&#36;</span>
                                    {item.price}
                                    <div className='iconfont'>&#xe6b2;</div>
                                </div>
                            </div>
                        )
                    })
                }

            </div>
        </div>
    )
}

export default Card;