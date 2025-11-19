import axios, {AxiosRequestConfig} from "axios";
import {useState, useRef, useCallback, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {message} from "./message";
import {API_CONFIG} from "../config/api";
import {
    STORAGE_TOKEN,
    CONTENT_TYPE_JSON,
    CONTENT_TYPE_HEADER,
    AUTHORIZATION_HEADER,
    BEARER_PREFIX,
    HTTP_STATUS,
    HTTP_METHODS,
    ROUTE_LOGIN,
    ERROR_MESSAGES,
    DEFAULT_LANGUAGE,
    STORAGE_LANGUAGE
} from "../constants/apiConstants";
import {getCurrentLanguage, subscribeLanguageChange} from "./i18n";

//默认请求参数
const defaultRequestConfig= {
    //default initial value
    url: '/',
    method: HTTP_METHODS.GET,
    data: {},
    params: {},
}
//define a custom hook to send request
//step2.T 就变成了传递进来的数据类型 ResponseType
function useRequest<T>(
    options: AxiosRequestConfig & {manual ? : boolean}= defaultRequestConfig//defined options an initial value
) {

    const navigate = useNavigate();



    //step3.data的类型定义为 ResponseType | null
    //data 要么是传递进来的数据类型，要么是null
    //data either has the type passed in, or is null
    const[data, setData] = useState<T | null>(null);
    const[error, setError] = useState('');
    const[loaded, setLoaded] = useState(false);
    //define a controller to stop req
    //它的改变不需要重新渲染，所以用useRef
    //it doesn't need to be re-rendered, so use useRef
    const controllerRef = useRef(new AbortController());
    const cancel = () => {
        controllerRef.current.abort();
    }

    //either passing or not passing options
    const request =  useCallback((requestOptions: AxiosRequestConfig) => {
        //每次请求之前先清空之前的状态
        //clear the previous state before each request
        setData(null);
        setError('');
        setLoaded(false);

        //bring login token to backend when sending request
        const loginToken = localStorage.getItem(STORAGE_TOKEN);
        const headers: any = {
            [CONTENT_TYPE_HEADER]: CONTENT_TYPE_JSON,
        };
        
        if (loginToken) {
            headers[AUTHORIZATION_HEADER] = `${BEARER_PREFIX}${loginToken}`;
        }

        // 允许请求覆盖默认的headers
        if (requestOptions.headers) {
            Object.assign(headers, requestOptions.headers);
        }

        // 自动添加语言参数到GET请求
        let params = requestOptions.params || {};
        if (requestOptions.method === HTTP_METHODS.GET || !requestOptions.method) {
            // 对于GET请求，自动添加lang参数
            const currentLanguage = getCurrentLanguage();
            params = {
                ...params,
                lang: currentLanguage
            };
        }

        //发送异步请求，捕捉异常
        //sending a request, catching an exception
        return axios.request<T>({
                //passing three parameters as obj
            //if passing, use requestOptions?.url
            //if not passing, use options.url instead.(outer)
                baseURL: API_CONFIG.BASE_URL,
                url: requestOptions.url,
                method: requestOptions.method,
                signal: controllerRef.current.signal,
                data: requestOptions.data,
                params: params,
                headers,
            // headers: {...headers, ...requestOptions.headers}, //added token to header
            }).then(response => {
                setData(response.data);
                return response.data;
            }).catch(e => {
                if(e?.response?.status === HTTP_STATUS.UNAUTHORIZED) {//401 means unauthorized
                    //if token is invalid, clear it
                    localStorage.removeItem(STORAGE_TOKEN);
                    //then directly redirect to login page
                    navigate(ROUTE_LOGIN);
                } else if(e?.response?.status === HTTP_STATUS.FORBIDDEN) {//403 means forbidden
                    //if token is expired or invalid, clear it and show friendly message
                    localStorage.removeItem(STORAGE_TOKEN);
                    message(ERROR_MESSAGES.TOKEN_EXPIRED, 2000);
                    //then redirect to login page
                    navigate(ROUTE_LOGIN);
                }

                setError(e.message || ERROR_MESSAGES.UNKNOWN_REQUEST_ERROR);
                throw e; // 保持原始错误对象，不要包装成新的Error
            }).finally(()=>{
                setLoaded(true);
            })
    },[navigate]);//依赖项为 options 和 navigate


    //传递参数发生变化，自动发送请求
    //useRequest 封装了useEffect
    // 注意：需要监听语言变化，当语言改变时重新请求
    useEffect(() => {
        if(!options.manual) {//if not manual, automatically send request
            //here should handle the exception thrown by request
            request(options).catch(e=>{
                message(e?.message, 1500);
            })

        }

    }, [options, request]); // request已经在内部使用了getCurrentLanguage()，会自动获取最新语言

    useEffect(() => {
        if (options.manual) {
            return;
        }

        const unsubscribe = subscribeLanguageChange(() => {
            request(options).catch(e => {
                message(e?.message, 1500);
            });
        });

        return () => {
            unsubscribe();
        };
    }, [options, request]);










    //step4. 把data 返回， 返回 data 的类型一定为 ResponseType | null
    return {data, error, loaded, request, cancel};
}

export default useRequest;