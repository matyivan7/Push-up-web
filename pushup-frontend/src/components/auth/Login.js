import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {login} from '../../service/authService';

const Login = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const credentials = {username, password};
            await login(credentials);
            navigate('/dashboard');
        } catch (error) {
            setError(error.message || "Login failed, please try again");
        }
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            {error && <div className="error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="login-username">Username: </label>
                    <input id="login-username" type="text" value={username}
                           onChange={(e) => setUsername(e.target.value)}
                           required/>
                </div>
                <div>
                    <label htmlFor="login-password">Password: </label>
                    <input id="login-password" type="password" value={password}
                           onChange={(e) => setPassword(e.target.value)}
                           required/>
                </div>
                <button type="submit">Login</button>
            </form>
        </div>
    );
};

export default Login;