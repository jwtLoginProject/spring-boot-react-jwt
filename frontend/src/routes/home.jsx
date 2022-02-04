import React from 'react';
import { Link } from 'react-router-dom';

const Home = ({getCurrentUser, signOut}) => {
    const currentUser = getCurrentUser();
    
    return (
        <>
        <Link to='/join'>        
            <button>Join</button>
        </Link>
        {currentUser ? 
        <button onClick={()=>signOut()}>Logout</button>
        :
        <Link to='/login'>
            <button>Login</button>
        </Link>
        }
        </>
    )
}

export default Home;