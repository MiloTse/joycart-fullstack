import type {LoginResponseType} from "./types";
import React, {useState} from "react";
import useRequest from "../../utils/useRequest";
import {useNavigate} from "react-router-dom";
import { message } from "../../utils/message";
import {API_ENDPOINTS} from "../../config/api";
import useLanguage from "../../hooks/useLanguage";
import {syncLanguageFromUser, translate, UI_TRANSLATION_KEYS} from "../../utils/i18n";
//1. 首先定义接口返回内容
// type ResponseType = {
//     status:string,
//     data:string
// }

const Login = ()=> {
     const [phoneNumber, setPhoneNumber] = useState('');
    const [password, setPassword] = useState('');
    //false means not select to agree to terms, true means select
    const [agreedToTerms, setAgreedToTerms] = useState(false);

    const navigate = useNavigate();
    const language = useLanguage();

    //use custom hook to send request
    //step1. 通过泛型传递给useRequest 方法
    //step5.接受data 类型也一定为 ResponseType | null
    // const { data, error, request, } = useRequest<ResponseData>('/charlestesta.json', 'GET', {});
    const {request, } = useRequest<LoginResponseType>({manual: true});

    function handleSubmitBtnClick() {
        if(!phoneNumber ) {
            // alert('please input phone number!');
            message(translate(UI_TRANSLATION_KEYS.messages.phoneRequired, language));
            return;
        }
        if(!password ) {
             message(translate(UI_TRANSLATION_KEYS.messages.passwordRequired, language));
            return;
        }
        if(!agreedToTerms) {
            message(translate(UI_TRANSLATION_KEYS.messages.agreeTerms, language));
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
        ).then((response)=>{
            console.log('=== Login API Response ===');
            console.log('Full response:', response);
            console.log('User data:', response.data);
            console.log('=========================');
            
            // 检查ResponseDTO格式的响应
            if(response.code === 200 && response.data.token) {
                localStorage.setItem('token', response.data.token);
                syncLanguageFromUser(response.data.languagePreference);
                message(response.message || 'Login Successfully！');
                //if login success, redirect to home page
                navigate('/home');
            } else {
                message(response.message || 'Login failed');
            }
         }).catch((e:any)=>{
             console.error('Login error:', e);
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
                    <div className='form-item-title'>{translate(UI_TRANSLATION_KEYS.login.phoneNumberLabel, language)}</div>
                    <input value={phoneNumber}
                           className='form-item-content'
                           placeholder={translate(UI_TRANSLATION_KEYS.login.phoneNumberPlaceholder, language)}
                           onChange={(e)=>{
                               setPhoneNumber(e.target.value);
                           }}
                    />
                </div>
                <div className="form-item">
                    <div className='form-item-title'>{translate(UI_TRANSLATION_KEYS.login.passwordLabel, language)}</div>
                    <input value={password}
                           type="password"
                           className="form-item-content"
                           placeholder={translate(UI_TRANSLATION_KEYS.login.passwordPlaceholder, language)}
                           onChange={(e)=>{
                               setPassword(e.target.value);
                           }}
                    />
                </div>
            </div>

            <div className="submit" onClick={handleSubmitBtnClick}>
                {translate(UI_TRANSLATION_KEYS.login.submitButton, language)}
            </div>
            <p className="notice">
                <input 
                    type="checkbox"
                    checked={agreedToTerms}
                    onChange={(e) => setAgreedToTerms(e.target.checked)}
                />
                {translate(UI_TRANSLATION_KEYS.login.agreePrefix, language)}
                <a href="#">{translate(UI_TRANSLATION_KEYS.login.termsLink, language)}</a>
                &
                <a href="#">{translate(UI_TRANSLATION_KEYS.login.privacyLink, language)}</a>
            </p>


        </>

)

}

export default Login