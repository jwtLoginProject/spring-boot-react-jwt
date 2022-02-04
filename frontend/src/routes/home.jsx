import React from 'react';
import { Link } from 'react-router-dom';

const Home = ({authService}) => {
    const currentUser = authService.getCurrentUser();

    return (
        <>
        <Link to='/join'>        
            <button>Join</button>
        </Link>
        {currentUser ? 
        <button onClick={authService.signOut()}>Logout</button>
        :
        <Link to='/login'>
            <button>Login</button>
        </Link>
        }
        </>
    )
}

export default Home;