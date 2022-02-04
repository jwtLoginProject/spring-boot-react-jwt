import axios from "axios";
import { customAxios } from "./customAxios";
import { Cookies } from 'react-cookie';
import jwt_decode from 'jwt-decode';

const cookies = new Cookies;

export default class AuthService {
    constructor() {
        this.accessToken = '';
        this.refreshToken = '';
    }

    signIn = async (user) => {
        const tokenSet = await customAxios.post('/auth/signInProc', JSON.stringify(user));
        // 쿠키 받아와서 프론트 쿠키에 저장
        if(tokenSet) {
            let access = cookies.set('accessToken');
            let refresh = cookies.set('refreshToken');
            localStorage.setItem('AuthenticatedUser', user.userId);
        }

        // accessToken 만료 기간 검증, 갱신
        this.refreshTokensBeforeExpire(cookies.get('accessToken'));
        
        // 쿠키에 저장된 토큰을 헤더에 저장
        axios.interceptors.request.use(config => {
            this.accessToken = cookies.get('accessToken');
            if (this.accessToken !== '') {
                config.headers['accessToken'] = 'Bearer ' + this.accessToken;
            }
            return config;
        });
    }
    
    signUp = async (user) => {
        const data = await customAxios.post('/auth/signUpProc', JSON.stringify(user));
        return data;
    }
    
    signOut = () => {
        // 쿠키에서 토큰 제거?
        localStorage.removeItem('AuthenticatedUser');
    }

    getCurrentUser = () => {
        return this.accessToken !== '' && localStorage.getItem('AuthenticatedUser');
    }

    refreshTokensBeforeExpire = () => {
        const { exp } = jwt_decode(this.accessToken);
        if(exp < 1000 * 60) {
            const tokens = await customAxios.post('', JSON.stringify());
            // 토큰 갱신
            let access = cookies.set('accessToken');
            let refresh = cookies.set('refreshToken');
        }
    }
}