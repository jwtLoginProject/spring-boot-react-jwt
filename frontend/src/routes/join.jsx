import React, { useState } from 'react';

const Join = ({authService}) => {
    const [formData, setFormData] = useState({
        userId: '',
        password: ''
    });

    const handleJoin = (user) => {
        authService.signUp(user);
    }

    return (
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
        <button onClick={()=>handleJoin(formData)}>Join</button>
        </>
    );
}

export default Join;