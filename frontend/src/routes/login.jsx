import React, { useState } from 'react';

const Login = ({signIn, authUser}) => {
    const [formData, setFormData] = useState({
        userId: '',
        password: ''
    });

    const handleLogin = (user) => {
        signIn(user);
    }

    return (
        <>
        {
            authUser ? 
            window.location.href = '/' :
            <>
            <form>
                <input type="text" 
                name="id"
                placeholder="id" 
                value={formData.userId}
                onChange={(e)=>{setFormData(oldData => ({...oldData, userId:e.target.value}))}}
                />
                <input type="password" 
                name="password"
                placeholder="password"
                value={formData.password}
                onChange={(e)=>{setFormData(oldData => ({...oldData, password:e.target.value}))}}
                />
            </form>
            <button onClick={()=>handleLogin(formData)}>Login</button>
            </>
        }
        </>
    );
}

export default Login;