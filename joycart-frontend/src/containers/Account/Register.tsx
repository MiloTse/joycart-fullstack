import type {RegisterResponseType} from "./types";
import React, {  useState} from "react";
import useRequest from "../../utils/useRequest";
import { message } from "../../utils/message";
import {useNavigate} from "react-router-dom";
import {API_ENDPOINTS} from "../../config/api";
//1. 首先定义接口返回内容
// type ResponseType = {
//     status:string,
//     data:string
// }

const Register = ()=> {
     const [userName, setUserName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [password, setPassword] = useState('');
    const [checkPassword, setCheckPassword] = useState('');
    const navigate = useNavigate();


    //use custom hook to send request
    //step1. 通过泛型传递给useRequest 方法
    //step5.接受data 类型也一定为 ResponseType | null
    // const { data, error, request, } = useRequest<ResponseData>('/charlestesta.json', 'GET', {});
    const {request, } = useRequest<RegisterResponseType>({manual: true});

    function handleSubmitBtnClick() {
        if(!userName) {
             message('userName should not be empty.');
            return;
        }
        if(!phoneNumber) {
            // alert('please input phone number!');
            message('phone number should not be empty.');
            return;
        }

        if(!password) {
            message('password should not be empty.');
            return;
        }

        if(password.length<6) {
            message('password length should not be less than 6.');
            return;
        }

        if(password!==checkPassword) {
            message('password should be same as checkPassword.');
            return;
        }




        request({
                url: API_ENDPOINTS.REGISTER,
                method:'POST',
                data:{
                    username:userName,
                    phoneNumber:phoneNumber,
                    password:password
                }
            }

        ).then((data)=>{
            data && console.log(data);
            // 后端成功返回User对象，包含id字段
            if(data?.id) {
                message('Register Successfully！');
                navigate('/account/login');
            }
        }).catch((e:any)=>{
            // 处理后端错误
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
                    <div className='form-item-title'>username</div>
                    <input value={userName}
                           className='form-item-content'
                           placeholder='please input username'
                           onChange={(e)=>{
                               setUserName(e.target.value);
                           }}
                    />
                </div>
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
                <div className="form-item">
                    <div className='form-item-title'>confirm password</div>
                    <input value={checkPassword}
                           type="password"
                           className="form-item-content"
                           placeholder="please re-input password"
                           onChange={(e)=>{
                               setCheckPassword(e.target.value);
                           }}
                    />
                </div>
            </div>

            <div className="submit" onClick={handleSubmitBtnClick}>
                register
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

export default Register