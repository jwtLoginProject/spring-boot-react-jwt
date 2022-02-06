import { useCookies } from 'react-cookie';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from '../src/routes/home';
import Join from './routes/join';
import Login from './routes/login';
import SayHello from './routes/sayHello';
import { customAxios } from './service/customAxios';
import axios from 'axios';
import jwt_decode from 'jwt-decode';

const App = () => {
  const [cookies, setCookie, removeCookie] = useCookies(['accessToken', 'refreshToken']);

  const signIn = async (user) => {
    const tokenSet = await customAxios.post('/auth/signInProc', JSON.stringify(user));
    if(tokenSet) {
        // 쿠키 받아와서 프론트 쿠키에 저장
        setCookie('accessToken', '');
        setCookie('refreshToken', '');
        // accessToken 만료 기간 검증, 갱신
        refreshTokensBeforeExpire(cookies.accessToken, user);

        // 로그인 user 아이디 웹스토리지 저장
        localStorage.setItem('AuthenticatedUser', user.userId);
    }

    // 쿠키에 저장된 토큰을 헤더에 저장
    axios.interceptors.request.use(config => {
        if (cookies.accessToken) {
            config.headers['accessToken'] = 'Bearer ' + cookies.accessToken;
        }
        return config;
    });
}

const signUp = async (user) => {
    const data = await customAxios.post('/auth/signUpProc', JSON.stringify(user));
    return data;
}

const signOut = () => {
    // 쿠키에서 토큰 제거?
    // 로그인 user 아이디 웹스토리지에서 제거
    localStorage.removeItem('AuthenticatedUser');
}

const getCurrentUser = () => {
    return cookies.accessToken !== '' && localStorage.getItem('AuthenticatedUser');
}

const refreshTokensBeforeExpire = async (token, user) => {
    const { exp } = jwt_decode(token);
    if(exp < 1000 * 60) {
        // accessToken 만료 1분 전, 헤더에 refresToken 과 함께 보내기
        axios.interceptors.request.use(config => {
            config.headers['accessToken'] = 'Bearer ' + cookies.accessToken;
            config.headers['refreshToken'] = 'Bearer ' + cookies.refreshToken;
            return config;
        });

        // 토큰 갱신
        const newTokenSet = await customAxios.post('/새로운api', JSON.stringify(user));
        setCookie('accessToken', '');
        setCookie('refreshToken', '');
    }
}
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home
        getCurrentUser={getCurrentUser}
        signOut={signOut}
        />} />
        <Route path="/join" element={<Join
        signUp={signUp}
        />} />
        <Route path="/login" element={<Login
        getCurrentUser={getCurrentUser}
        signIn={signIn}
        />} />
        <Route path="/hello" element={<SayHello
        getCurrentUser={getCurrentUser}
        />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
