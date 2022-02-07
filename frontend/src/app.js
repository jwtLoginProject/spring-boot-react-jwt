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
  const [cookies, setCookie, removeCookie] = useCookies(['accessToken']);

  const signIn = async (user) => {
    try {
      const tokenSet = await customAxios.post('/auth/signInProc', JSON.stringify(user));
      // // 쿠키 받아와서 프론트 쿠키에 저장
      setCookie('accessToken', tokenSet.data.AccessToken);
      
      // // 로그인 user 아이디, refreshToken 웹스토리지 저장
      localStorage.setItem('AuthenticatedUser', user.userId);
      localStorage.setItem('refreshToken', tokenSet.data.RefreshToken);

      window.location.href = '/hello';
    } catch {

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
      // 쿠키에서 토큰 제거
      removeCookie('accessToken');
      // 웹스토리지 토큰 제거
      localStorage.removeItem('refreshToken');
      // 로그인 user 아이디 웹스토리지에서 제거
      localStorage.removeItem('AuthenticatedUser');
  }

  const getCurrentUser = () => {
      return cookies.accessToken !== '' && localStorage.getItem('AuthenticatedUser');
  }
  

  const getNewAccessTokenBeforeExpire = async (token, user) => {
    const currTime = Date.now()/1000;
    const { exp } = jwt_decode(token);
    let newUser = user;
    if(exp < currTime + 1000 * 10) {
        newUser['refreshToken'] = 'Bearer ' + localStorage.getItem('refreshToken');
        axios.interceptors.request.use(config => {
            config.headers['accessToken'] = 'Bearer ' + cookies.accessToken;
            return config;
          });

          // 토큰 갱신
          const newTokenSet = await customAxios.post('/auth/refreshToken', JSON.stringify(newUser));
          if(newTokenSet.data.RefreshToken) {
            setCookie('accessToken', newTokenSet.data.AccessToken);
            localStorage.setItem('refreshToken', newTokenSet.data.RefreshToken);
          }else{
            setCookie('accessToken', newTokenSet.data.AccessToken);
          }
        }
    }


  const getNewRefreshTokenBeforeExpire = async (token, user) => {
      const currTime = Date.now()/1000;
      const { exp } = jwt_decode(token);
      let newUser = user;

      if(exp < currTime + 1000 * 30) {
          newUser['refreshToken'] = 'Bearer ' + localStorage.getItem('refreshToken');
          axios.interceptors.request.use(config => {
              config.headers['accessToken'] = 'Bearer ' + cookies.accessToken;
              return config;
            });

            // 토큰 갱신
            const newTokenSet = await customAxios.post('/auth/refreshToken', JSON.stringify(newUser));
            if(newTokenSet.data.RefreshToken) {
              setCookie('accessToken', newTokenSet.data.AccessToken);
              localStorage.setItem('refreshToken', newTokenSet.data.RefreshToken);
            }else{
              setCookie('accessToken', newTokenSet.data.AccessToken);
            }
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
          access={cookies.accessToken}
          refresh={cookies.refreshToken}
          getAccess={getNewAccessTokenBeforeExpire}
          getRefresh={getNewRefreshTokenBeforeExpire}
          />} />
        </Routes>
      </BrowserRouter>
    );
}

export default App;
