import type {RegisterResponseType} from "./types";
import React, {  useState} from "react";
import useRequest from "../../utils/useRequest";
import { message } from "../../utils/message";
import {useNavigate} from "react-router-dom";
import {API_ENDPOINTS} from "../../config/api";
import useLanguage from "../../hooks/useLanguage";
import {LANGUAGE_OPTIONS, getCurrentLanguage, setLanguagePreference, translate, UI_TRANSLATION_KEYS} from "../../utils/i18n";
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
    const [languagePreference, setLanguagePreferenceState] = useState(getCurrentLanguage());
    const navigate = useNavigate();
    const language = useLanguage();


    //use custom hook to send request
    //step1. 通过泛型传递给useRequest 方法
    //step5.接受data 类型也一定为 ResponseType | null
    // const { data, error, request, } = useRequest<ResponseData>('/charlestesta.json', 'GET', {});
    const {request, } = useRequest<RegisterResponseType>({manual: true});

    function handleSubmitBtnClick() {
        if(!userName) {
             message(translate(UI_TRANSLATION_KEYS.messages.usernameRequired, language));
            return;
        }
        if(!phoneNumber) {
            // alert('please input phone number!');
            message(translate(UI_TRANSLATION_KEYS.messages.phoneRequired, language));
            return;
        }

        if(!password) {
            message(translate(UI_TRANSLATION_KEYS.messages.passwordRequired, language));
            return;
        }

        if(password.length<6) {
            message(translate(UI_TRANSLATION_KEYS.messages.passwordTooShort, language));
            return;
        }

        if(password!==checkPassword) {
            message(translate(UI_TRANSLATION_KEYS.messages.passwordMismatch, language));
            return;
        }




        request({
                url: API_ENDPOINTS.REGISTER,
                method:'POST',
                data:{
                    username:userName,
                    phoneNumber:phoneNumber,
                    password:password,
                    languagePreference: languagePreference
                }
            }

        ).then((response)=>{
            console.log('=== Register API Response ===');
            console.log('Full response:', response);
            console.log('User data:', response.data);
            console.log('============================');
            
            // 检查ResponseDTO格式的响应
            if(response.code === 200) {
                // 保存语言偏好到localStorage
                setLanguagePreference(languagePreference);
                message(response.message || 'Register Successfully！');
                navigate('/account/login');
            } else {
                message(response.message || 'Registration failed');
            }
        }).catch((e:any)=>{
            console.error('Register error:', e);
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
                    <div className='form-item-title'>{translate(UI_TRANSLATION_KEYS.register.usernameLabel, language)}</div>
                    <input value={userName}
                           className='form-item-content'
                           placeholder={translate(UI_TRANSLATION_KEYS.register.usernamePlaceholder, language)}
                           onChange={(e)=>{
                               setUserName(e.target.value);
                           }}
                    />
                </div>
                <div className="form-item">
                    <div className='form-item-title'>{translate(UI_TRANSLATION_KEYS.register.phoneNumberLabel, language)}</div>
                    <input value={phoneNumber}
                           className='form-item-content'
                           placeholder={translate(UI_TRANSLATION_KEYS.register.phoneNumberPlaceholder, language)}
                           onChange={(e)=>{
                               setPhoneNumber(e.target.value);
                           }}
                    />
                </div>
                <div className="form-item">
                    <div className='form-item-title'>{translate(UI_TRANSLATION_KEYS.register.passwordLabel, language)}</div>
                    <input value={password}
                           type="password"
                           className="form-item-content"
                           placeholder={translate(UI_TRANSLATION_KEYS.register.passwordPlaceholder, language)}
                           onChange={(e)=>{
                               setPassword(e.target.value);
                           }}
                    />
                </div>
                <div className="form-item">
                    <div className='form-item-title'>{translate(UI_TRANSLATION_KEYS.register.confirmPasswordLabel, language)}</div>
                    <input value={checkPassword}
                           type="password"
                           className="form-item-content"
                           placeholder={translate(UI_TRANSLATION_KEYS.register.confirmPasswordPlaceholder, language)}
                           onChange={(e)=>{
                               setCheckPassword(e.target.value);
                           }}
                    />
                </div>
                <div className="form-item">
                    <div className='form-item-title'>{translate(UI_TRANSLATION_KEYS.register.languagePreferenceLabel, language)}</div>
                    <select 
                        value={languagePreference}
                        className="form-item-content"
                        onChange={(e) => {
                            const value = e.target.value;
                            setLanguagePreferenceState(value);
                            setLanguagePreference(value);
                        }}
                    >
                        {LANGUAGE_OPTIONS.map(option => (
                            <option key={option.code} value={option.code}>
                                {option.nativeName} ({option.name})
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            <div className="submit" onClick={handleSubmitBtnClick}>
                {translate(UI_TRANSLATION_KEYS.register.submitButton, language)}
            </div>
            <p className="notice">
                <input type="checkbox"/>
                {translate(UI_TRANSLATION_KEYS.register.agreePrefix, language)}
                <a href="#">{translate(UI_TRANSLATION_KEYS.register.termsLink, language)}</a>
                &
                <a href="#">{translate(UI_TRANSLATION_KEYS.register.privacyLink, language)}</a>
            </p>

        </>


    )

}

export default Register