import React, {useState} from 'react';
import './style.scss';
import {Link, useParams} from 'react-router-dom';
import useRequest from "../../utils/useRequest";
import type {ResponseType} from "./types";

const SearchList = () => {
    const params = useParams<{ shopId : string; keyword: string}>();
    const [keyword, setKeyword] = useState(params.keyword);
    const [tabValue,setTabValue] = useState('default');
    const [requestData, setRequestData] = useState({
        url: '/shopSearchList.json',
        method: 'GET',
        params: {
            keyword: params.keyword,
            shopId: params.shopId,
            // page: 0,
            // pageSize: 8,
            type: tabValue
        },

    });
    const {data} = useRequest<ResponseType>(requestData);
    console.log(data);
    const list = data?.data || [];

    function handleClearKeyword() {
        setKeyword('');
    }

    function handleKeyDown(key: string) {
        if(key === 'Enter' && keyword) {
            //更新本地存储内容
            //update local storage content
            const localSearchList= localStorage.getItem('search-list');
            const searchHistory: string[] = localSearchList ? JSON.parse(localSearchList) : [];



            const keywordIndex = searchHistory.findIndex(item=>item === keyword);
            //add search keyword to historyList when can't find the keyword in history list
            const newHistoryList = [...searchHistory];
            //if search keyword existed, remove it from the list from keywordIndex.
            if(keywordIndex > -1){
                newHistoryList.splice(keywordIndex, 1);
            }
            //put new keyword at the beginning of the list
            newHistoryList.unshift(keyword);
            if(newHistoryList.length > 5) {
                newHistoryList.pop();
            }
             localStorage.setItem('search-list', JSON.stringify(newHistoryList));
            //request data. old data assign to new data
            const newRequestData = {
                ...requestData,
            }
            //current key assign to newRequestData's keyword when data changed
            newRequestData.params.keyword = keyword;
            setRequestData(newRequestData);
        }
    }

    function handleTabClick(tabValue: string) {
        setTabValue(tabValue);

        const newRequestData = {
            ...requestData,
        }
         newRequestData.params.type = tabValue;
        setRequestData(newRequestData);
    }

    return (
        <div className="page search-list-page">
            {/*search input area*/}
            <div className="search">
                <Link to={`/search/${params.shopId}`} className="search-back-link">
                    <div className="search-back-icon iconfont">&#xe6a9;</div>
                </Link>
                <div className="search-area">
                    <div className="search-icon iconfont">&#xe600;</div>
                    <input
                        className="search-input"
                        placeholder="Please enter product name"
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                        onKeyDown={(e) => {handleKeyDown(e.key)} }
                    />
                </div>
                <div className="search-clear iconfont" onClick={handleClearKeyword}>&#xe610;</div>
            </div>
            {/*item list area*/}
            <div className="tab">
                <div className={tabValue==='default'? "tab-item tab-item-active" : "tab-item " } onClick={()=>handleTabClick('default')}>Default</div>
                <div className={tabValue==='sales'? "tab-item tab-item-active" : "tab-item " }   onClick={()=>handleTabClick('sales')}>Sale</div>
                <div className={tabValue==='price'? "tab-item tab-item-active" : "tab-item " }   onClick={()=>handleTabClick('price')}>Price</div>

            </div>
            {/*product list area*/}
            <div className="list">
                {
                    list.map(item=>{
                        return (
                            <Link to={`/detail/${item.id}`} >
                                <div className="item" key={item.id}>
                                    <img className="item-img" alt={item.title} src={item.imgUrl}/>
                                    <div className="item-content">
                                        <p className="item-title">
                                            {item.title}
                                        </p>
                                        <div className="item-price">
                                        <span className="item-price-symbol">
                                            &#36;
                                        </span>
                                            {item.price}
                                        </div>
                                        <div className="item-sales">sold {item.sales} </div>
                                    </div>
                                </div>

                            </Link>


                        )

                    })
                }


</div>
{/*bottom line*/
}
<div className="bottom">
    -- I am the bottom line --
</div>
</div>
)
}

export default SearchList;