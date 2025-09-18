import './style.scss';
import {useNavigate, useParams} from "react-router-dom";
import {useState} from "react";
import useRequest from "../../utils/useRequest";
import type {ResponseType} from "./types";


//defaultRequestData
const defaultRequestData = {
    url: '/hotSearchList.json',
    method: 'GET',
    params: { shopId: ''},
}

const Search = () => {
    const {data} = useRequest<ResponseType>(defaultRequestData);
    const hotList = data?.data || [];




    const localSearchList= localStorage.getItem('search-list');
    const searchHistory: string[] = localSearchList ? JSON.parse(localSearchList) : [];


    const navigate = useNavigate();
    const handleGoBackClick = () => {
        navigate('/home');
    };
    //load search history from local storage
    const [historyList, setHistoryList] = useState (searchHistory);
    const [keyword, setKeyword] = useState('');
     //gain shopId from url
    const params = useParams<{shopId:string}>();
    // console.log("shopId",params.shopId);
    if(params.shopId){
        defaultRequestData.params.shopId = params.shopId;
    }

    function handleKeyDown(key: string) {
        if(key === 'Enter' && keyword) {
            const keywordIndex = historyList.findIndex(item=>item === keyword);
            //add search keyword to historyList when can't find the keyword in history list
            const newHistoryList = [...historyList];
            //if search keyword existed, remove it from the list from keywordIndex.
            if(keywordIndex > -1){
                 newHistoryList.splice(keywordIndex, 1);
            }
            //put new keyword at the beginning of the list
            newHistoryList.unshift(keyword);
            if(newHistoryList.length > 5) {
                newHistoryList.pop();
            }
            setHistoryList(newHistoryList);
            localStorage.setItem('search-list', JSON.stringify(newHistoryList));
            //navigate to searchList page
            navigate(`/searchList/${params.shopId}/${keyword}`);
            setKeyword('');

        }
    }

    function handleHistoryListClean(){
        setHistoryList([]);
        localStorage.setItem('search-list', JSON.stringify([]));
    }

    function handleKeywordClick(keyword: string) {
        navigate(`/searchList/${params.shopId}/${keyword}`);
    }

    return (
        <div className="page search-page">
            <div className="search">
                <div className="search-back-icon iconfont"  onClick={handleGoBackClick} style={{ cursor: 'pointer' }}>
                    &#xe6a9;</div>
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
            </div>
            {
                historyList.length? (
                    <>
                        <div className="title">
                            History Search
                            <div onClick={handleHistoryListClean}
                                 className="iconfont title-close">
                                &#xe610;
                            </div>
                        </div>
                        <ul className="list">
                            {
                                historyList.map((item, index) => {
                                    return (
                                        <li key={index + item} className='list-item' onClick={()=>handleKeywordClick(item)} >
                                            {item}
                                        </li>
                                    )
                                })
                            }
                        </ul>
                    </>
                ) : null
            }
            {
                hotList.length? (
                    <>
                        <div className="title">
                            Hot Search
                        </div>
                        <ul className="list">
                            {
                                hotList.map(item =>
                                    (
                                        <li key={item.id} className='list-item' onClick={()=>handleKeywordClick(item.keyword)} >
                                            {item.keyword}
                                        </li>
                                    )
                                )
                            }
                         </ul>

                    </>


                ) : null
            }

        </div>
    );
};

export default Search;