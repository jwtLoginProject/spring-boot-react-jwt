
const SayHello = ({authUser}) => {
    const userId = localStorage.getItem('userId') || null;

    return (
        <>
        { authUser ?
            <h1>Hello, {userId}</h1>
        :   window.location.href = '/'
        }
        </>
    );
}

export default SayHello;
