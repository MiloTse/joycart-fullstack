import type {LoginResponseType} from "./types";
import React, {useRef, useState} from "react";
import useRequest from "../../utils/useRequest";
import {useNavigate} from "react-router-dom";
import { message } from "../../utils/message";
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
                url: '/login.json',
                method: 'GET',
                // The original POST request code was implemented using Charles Proxy, and it is currently commented out to facilitate future conversion into a full-stack project.
                // method: 'POST',
                // data: {
                //     phoneNumber: phoneNumber,
                //     password: password,
                // }
            }
        ).then((data)=>{
            data && console.log(data);
            //validate
            const {data: token } = data;
            console.log(token)
            if(token) {
                localStorage.setItem('token', token);
                //if login success, redirect to home page
                navigate('/home');
            }
         }).catch((e:any)=>{
             // alert(e?.message);
            // setShowModal(true);
            // setMessage(e?.message || 'unknown error.');
            message(e?.message || 'unknown error.');
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