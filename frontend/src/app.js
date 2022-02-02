import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from '../src/routes/home';
import Join from './routes/join';
import Login from './routes/login';
import SayHello from './routes/sayHello';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home/>} />
        <Route path="/join" element={<Join/>} />
        <Route path="/login" element={<Login/>} />
        <Route path="/hello" element={<SayHello/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
