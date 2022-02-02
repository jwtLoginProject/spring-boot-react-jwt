import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
    return (
        <>
        <Link to='/join'>        
            <button>Join</button>
        </Link>
        <Link to='/login'>
            <button>Login</button>
        </Link>
        </>
    )
}

export default Home;