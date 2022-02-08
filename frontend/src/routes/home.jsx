import React from 'react';
import { Link } from 'react-router-dom';

const Home = ({signOut, authUser}) => {
    return (
        <>
        <Link to='/join'>        
            <button>Join</button>
        </Link>
        {authUser ? 
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