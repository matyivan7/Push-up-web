import React from "react";
import './App.css';
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./components/auth/login/Login";
import Register from "./components/auth/register/Register";
import Dashboard from "./components/dashboard/Dashboard";
import PushUpForm from "./components/pushUpForm/pushUpForm";
import Navbar from "./components/navbar/Navbar";


const App = ()=> {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/navbar" element={<Navbar />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/new-session" element={<PushUpForm />} />
      </Routes>
    </Router>
  )
}

export default App;
