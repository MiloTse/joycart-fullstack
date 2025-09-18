import 'swiper/css';
import {Swiper, SwiperSlide} from "swiper/react";
import React, {useState} from "react";
import {BannersType, LocationType} from "../types";
import { useNavigate } from 'react-router-dom';

//outer passing props might be undefined
type BannerPropsType = {
    location: LocationType | undefined;
    banners: BannersType | undefined;
}


const Banner = (props: BannerPropsType) => {
    const navigate = useNavigate();
    const [page, setPage] = useState(1);
    //passing location and banners from props
    const {location, banners} = props;

    const handleLocationClick = () => {
        navigate('/nearby');
    };

    const handleSearchClick = () => {
        //在typescript中叫做类型收紧.use if condition or ? to avoid undefined
        navigate(`/search/${location?.id}`);
        if(location){
            navigate(`/search/${location.id}`);
        }
    };

    return (
        <div className="banner">
            <h3 className="location" onClick={handleLocationClick} style={{ cursor: 'pointer' }}>
                <span className="iconfont">&#xe68e;</span>
                {location?.address || ''}
            </h3>
            <div className="search" onClick={handleSearchClick} style={{ cursor: 'pointer' }}>
                <span className="iconfont">&#xe600;</span>
                Input search words here
            </div>
            <div className="swiper-area">
                <Swiper
                    spaceBetween={0}
                    slidesPerView={1}
                    onSlideChange={(e: any) => {
                        setPage(e.activeIndex + 1)
                    }}
                >
                    {
                        (
                            banners || []
                        ).map(
                            item => {
                                return (

                                    <SwiperSlide key={item.id}>
                                        <div className="swiper-item">
                                            <img className="swiper-item-img" src={item.imgUrl} alt='轮播图'/>
                                        </div>
                                    </SwiperSlide>
                                )
                            }
                        )
                    }


                </Swiper>
                <div className="pagination">{page}/{banners?.length || 0}</div>
            </div>
        </div>
    )
}

export default Banner;