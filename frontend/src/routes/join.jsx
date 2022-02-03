import React, { useState } from 'react';

const Join = () => {
    const [formData, setFormData] = useState({
        id: '',
        password: ''
    });

    const handleJoin = (formData) => {
        
    }
    return (
        <>
        <form>
            <input type="text" 
            name="id"
            placeholder="id" 
            value={formData.id}
            onChange={(e)=>{setFormData(oldData => ({...oldData, id:e.target.value}))}}
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