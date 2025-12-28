import './style.scss';
import {useNavigate, useParams} from "react-router-dom";
import {useState, useEffect} from "react";
import useRequest from "../../utils/useRequest";
import type {ResponseType} from "./types";
import {API_ENDPOINTS} from "../../config/api";
import useLanguage from "../../hooks/useLanguage";
import {translate, UI_TRANSLATION_KEYS} from "../../utils/i18n";


//defaultRequestData - 改为调用真实后端API
//defaultRequestData - changed to call real backend API
const defaultRequestData = {
    url: API_ENDPOINTS.SEARCH_HOT,
    method: 'GET',
    params: { shopId: ''},
}

const Search = () => {
    const {data} = useRequest<ResponseType>(defaultRequestData);
    const language = useLanguage();
    
    // 添加ResponseDTO格式的调试日志
    useEffect(() => {
        if (data) {
            console.log('=== Search Hot API Response ===');
            console.log('Full response:', data);
            console.log('Hot list:', data.data);
            console.log('==============================');
        }
    }, [data]);
    
    const hotList = data?.code === 200 ? data.data : [];




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
                           placeholder={translate(UI_TRANSLATION_KEYS.search.placeholder, language)}
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
                            {translate(UI_TRANSLATION_KEYS.search.historyTitle, language)}
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
                            {translate(UI_TRANSLATION_KEYS.search.hotTitle, language)}
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