import axios from "axios";
import { customAxios } from "./customAxios";
import { Cookies, withCookies } from 'react-cookie';
import jwt_decode from 'jwt-decode';

const cookies = new Cookies;

class AuthService {
    constructor() {
        this.accessToken = cookies.get('accessToken');
        this.refreshToken = cookies.get('refreshToken');
    }

    signIn = async (user) => {
        const tokenSet = await customAxios.post('/auth/signInProc', JSON.stringify(user));
        if(tokenSet) {
            // accessToken 만료 기간 검증, 갱신
            this.refreshTokensBeforeExpire(cookies.get('accessToken'), user);
            
            // 쿠키 받아와서 프론트 쿠키에 저장
            cookies.set('accessToken');
            cookies.set('refreshToken');

            // 로그인 user 아이디 웹스토리지 저장
            localStorage.setItem('AuthenticatedUser', user.userId);
        }

        // 쿠키에 저장된 토큰을 헤더에 저장
        axios.interceptors.request.use(config => {
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

    refreshTokensBeforeExpire = async (token, user) => {
        const { exp } = jwt_decode(token);
        if(exp < 1000 * 60) {
            // accessToken 만료 1분 전, 헤더에 refresToken 과 함께 보내기
            axios.interceptors.request.use(config => {
                config.headers['accessToken'] = 'Bearer ' + this.accessToken;
                config.headers['refreshToken'] = 'Bearer ' + this.refreshToken;
                return config;
            });

            // 토큰 갱신
            const newTokenSet = await customAxios.post('/', JSON.stringify(user));
            this.accessToken = cookies.get('accessToken');
            this.refreshToken = cookies.get('refreshToken');
        }
    }
}

export default withCookies(AuthService);