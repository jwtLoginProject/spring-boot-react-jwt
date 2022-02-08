import axios, {
    AxiosError,
    AxiosInstance,
    AxiosRequestConfig,
    AxiosResponse,
} from "axios";

const API_URL = "http://localhost:8000";

const onRequest = (config) => {
    const accessToken = JSON.parse(localStorage.getItem('accessToken'));
    if(accessToken){
        config.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    return config;
};

const onRequestError = (error) => {
    return Promise.reject(error);
};

const onResponse = (response) => {
    return response;
};

const onResponseError = async (error) => {
    if (error.response) {
    // Access Token was expired
    if (error.response.status === 401) {
        const oldRefreshToken = JSON.parse(localStorage.getItem('refreshToken'));
        const userId = JSON.parse(localStorage.getItem('userId'));

        try {
        const rs = await axios.post(`${API_URL}/auth/refreshToken`, {
            refreshToken: oldRefreshToken,
            userId
        });

        const { accessToken, refreshToken } = rs.data;

        localStorage.setItem('accessToken', JSON.stringify(accessToken));
        localStorage.setItem('refreshToken', JSON.stringify(refreshToken));
    
        return;
        } catch (_error) {
        return Promise.reject(_error);
        }
    }
    }
    return Promise.reject(error);
};

export const setUpInterceptorsTo = (axiosInstance => {
    axiosInstance.interceptors.request.use(onRequest, onRequestError);
    axiosInstance.interceptors.response.use(onResponse, onResponseError);
    return axiosInstance;
});