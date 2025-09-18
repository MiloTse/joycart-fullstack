import axios, {AxiosRequestConfig} from "axios";
import {useState, useRef, useCallback, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {message} from "./message";

//默认请求参数
const defaultRequestConfig= {
    //default initial value
    url: '/',
    method: 'GET',
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
        const loginToken = localStorage.getItem('token');
        const headers = loginToken ? {
            token: loginToken,
            Authorization: `Bearer ${loginToken}`,
        } : {};

        //发送异步请求，捕捉异常
        //sending a request, catching an exception
        return axios.request<T>({
                //passing three parameters as obj
            //if passing, use requestOptions?.url
            //if not passing, use options.url instead.(outer)
                baseURL: '',
                url: requestOptions.url,
                method: requestOptions.method,
                signal: controllerRef.current.signal,
                data: requestOptions.data,
                params: requestOptions.params,
                headers,
            // headers: {...headers, ...requestOptions.headers}, //added token to header
            }).then(response => {
                setData(response.data);
                return response.data;
            }).catch(e => {
                if(e?.resopnse?.status === 401) {//401 means unauthorized
                    //if token is invalid, clear it
                    localStorage.removeItem('token');
                    //then directly redirect to login page
                    navigate('/account/login');

                }

                setError(e.message || 'unknown request error.');
                throw new Error(e);
            }).finally(()=>{
                setLoaded(true);
            })
    },[navigate]);//依赖项为 options 和 navigate


    //传递参数发生变化，自动发送请求
    //useRequest 封装了useEffect
    useEffect(() => {
        if(!options.manual) {//if not manual, automatically send request
            //here should handle the exception thrown by request
            request(options).catch(e=>{
                message(e?.message, 1500);
            })

        }

    }, [options,request]);










    //step4. 把data 返回， 返回 data 的类型一定为 ResponseType | null
    return {data, error, loaded, request, cancel};
}

export default useRequest;