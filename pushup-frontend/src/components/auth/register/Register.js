import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {registerHandle} from '../../../service/authService';
import './register.css';

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
            <div className="register-box">
                <h2>Registration</h2>
                {error && <div className="error">{error}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="form-input">
                        <label htmlFor="username">Username: </label>
                        <input
                            id="username"
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-input">
                        <label htmlFor="register-password">Password: </label>
                        <input
                            id="register-password"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="register-btn">Register</button>
                </form>
            </div>
        </div>
    );
};

export default Register;