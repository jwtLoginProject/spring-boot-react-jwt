import axios from 'axios';
import { setUpInterceptorsTo } from './interceptors';

export const customAxios = setUpInterceptorsTo(
  axios.create({
    baseURL: "http://localhost:8000",
    headers: {
      "Accept" : "application/json",
      "Content-Type" : "application/json"
    },
    withCredentials: true
  })
);