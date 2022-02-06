import React from 'react';

const SayHello = ({getCurrentUser}) => {
    const currentUser = getCurrentUser();

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