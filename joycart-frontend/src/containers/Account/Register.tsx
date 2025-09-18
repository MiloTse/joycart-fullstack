import type {RegisterResponseType} from "./types";
import React, {  useState} from "react";
import useRequest from "../../utils/useRequest";
import { message } from "../../utils/message";
import {useNavigate} from "react-router-dom";
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
                url:'/register.json',
                method:'GET',
                // The original POST request code was implemented using Charles Proxy, and it is currently commented out to facilitate future conversion into a full-stack project.
                // method:'POST',
                // data:{
                //     userName:userName,
                //     phoneNumber:phoneNumber,
                //     password:password
                // }
            }

        ).then((data)=>{
            data && console.log(data);
            if(data?.status==='success') {
                navigate('/account/login');
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