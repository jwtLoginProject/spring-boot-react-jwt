import axios from 'axios';

export const customAxios = axios.create({
  baseURL: "http://localhost:8000",
  headers: {
    "Accept" : "application/json",
    "Content-Type" : "application/json"
  },
  withCredentials: true
});

// Add a request interceptor



// return authorization header with jwt token
// export function authHeader() {
//   const currentUser = authenticationService.currentUserValue;
//   if (currentUser && currentUser.token) {
//       return { Authorization: `Bearer ${currentUser.token}` };
//   } else {
//       return {};
//   }
// }
