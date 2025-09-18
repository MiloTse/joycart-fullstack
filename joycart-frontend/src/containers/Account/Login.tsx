import type {LoginResponseType} from "./types";
import React, {useState} from "react";
import useRequest from "../../utils/useRequest";
import {useNavigate} from "react-router-dom";
import { message } from "../../utils/message";
import {API_ENDPOINTS} from "../../config/api";
//1. 首先定义接口返回内容
// type ResponseType = {
//     status:string,
//     data:string
// }

const Login = ()=> {
     const [phoneNumber, setPhoneNumber] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    //use custom hook to send request
    //step1. 通过泛型传递给useRequest 方法
    //step5.接受data 类型也一定为 ResponseType | null
    // const { data, error, request, } = useRequest<ResponseData>('/charlestesta.json', 'GET', {});
    const {request, } = useRequest<LoginResponseType>({manual: true});

    function handleSubmitBtnClick() {
        if(!phoneNumber ) {
            // alert('please input phone number!');
            message('phone number should not be empty.');
            return;
        }
        if(!password ) {
             message('password should not be empty.');
            return;
        }
        request(
            {
                url: API_ENDPOINTS.LOGIN,
                method: 'POST',
                data: {
                    phoneNumber: phoneNumber,
                    password: password,
                }
            }
        ).then((data)=>{
            data && console.log(data);
            // 根据后端LoginResponseDTO格式处理响应
            if(data.status === 'success' && data.data.token) {
                localStorage.setItem('token', data.data.token);
                message('Login Successfully！');
                //if login success, redirect to home page
                navigate('/home');
            }
         }).catch((e:any)=>{
             // 处理后端错误
             console.error('Login error:', e);
             console.log('Error response data:', e?.response?.data);
             
             let errorMessage = 'Unknown error occurred';
             
             // 检查后端返回的结构化错误信息
             if (e?.response?.data?.message) {
                 errorMessage = e.response.data.message;
             } else if (e?.response?.data && typeof e.response.data === 'string') {
                 errorMessage = e.response.data;
             } else if (e?.message) {
                 errorMessage = e.message;
             }
             
             message(errorMessage);
        });
    }

    return (
        <>
            <div className="form">
                <div className="form-item">
                    <div className='form-item-title'>phone number</div>
                    <input value={phoneNumber}
                           className='form-item-content'
                           placeholder='please input phone number'
                           onChange={(e)=>{
                               setPhoneNumber(e.target.value);
                           }}
                    />
                </div>
                <div className="form-item">
                    <div className='form-item-title'>password</div>
                    <input value={password}
                           type="password"
                           className="form-item-content"
                           placeholder="please input password"
                           onChange={(e)=>{
                               setPassword(e.target.value);
                           }}
                    />
                </div>
            </div>

            <div className="submit" onClick={handleSubmitBtnClick}>
                login
            </div>
            <p className="notice">
                <input type="checkbox"/>
                I accept the
                <a href="#">Terms and Conditions</a>
                &
                <a href="#">Privacy Policy</a>
            </p>


        </>

)

}

export default Login