import React from 'react';

const SayHello = ({access, refresh, getCurrentUser, getAccess, getRefresh, user}) => {
    const currentUser = getCurrentUser();
    getAccess(access, user);
    getRefresh(refresh, user);
    
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