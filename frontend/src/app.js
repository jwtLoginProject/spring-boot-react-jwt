import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from '../src/routes/home';
import Join from './routes/join';
import Login from './routes/login';
import SayHello from './routes/sayHello';

const App = ({authService}) => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home
        authService={authService}
        />} />
        <Route path="/join" element={<Join
        authService={authService}
        />} />
        <Route path="/login" element={<Login
        authService={authService}
        />} />
        <Route path="/hello" element={<SayHello
        authService={authService}
        />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
