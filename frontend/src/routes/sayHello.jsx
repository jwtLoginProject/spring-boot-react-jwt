import React from 'react';

const SayHello = ({authService}) => {
    const currentUser = authService.getCurrentUser();

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