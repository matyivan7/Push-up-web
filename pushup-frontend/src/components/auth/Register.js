import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {registerHandle} from '../../service/authService';

const Register = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const userData = { username, password };
            await registerHandle(userData);
            navigate('/login');
        } catch (error) {
            setError("Registration failed, please try again");
        }
    };

    return (
        <div className="register-container">
            <h2>Register</h2>
            {error && <div
                className="error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor = "username">Username: </label>
                    <input id="username" type="text" value={username}
                           onChange={(e) => setUsername(e.target.value)}
                           required/>
                </div>
                <div>
                    <label htmlFor="register-password">Password: </label>
                    <input id="register-password" type="password" value={password}
                           onChange={(e) => setPassword(e.target.value)}
                           required/>
                </div>
                <button type="submit">Register</button>
            </form>
        </div>
    );
};

export default Register;