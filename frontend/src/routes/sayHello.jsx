import React from 'react';

const SayHello = ({access, refresh, getCurrentUser, getAccess, getRefresh}) => {
    const currentUser = getCurrentUser();
    const user = localStorage.getItem('user');

    getAccess(access, JSON.parse(user));
    getRefresh(refresh, JSON.parse(user));

    return (
        <>
        {currentUser ?
            <h1>Hello, {currentUser}</h1>
        :   window.location.href = '/'
        }
        </>
    );
}

export default SayHello;