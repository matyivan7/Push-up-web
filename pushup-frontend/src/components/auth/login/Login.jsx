import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {login} from '../../../service/authService';

const Login = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const credentials = {username, password};
            const userDetails = await login(credentials);
            navigate('/dashboard');
        } catch (error) {
            setError(error.message || "Login failed, please try again");
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
                <h2 className="text-2xl font-bold text-center text-gray-700 mb-6">Login</h2>
                {error && <div className="text-red-500 text-sm mb-4">{error}</div>}
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div className="relative z-0 w-full group">
                        <input
                            type="text"
                            id="username"
                            className="block py-2.5 px-2 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                            placeholder=" "
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                        <label
                            htmlFor="username"
                            className="absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6 peer-focus:text-blue-600"
                        >
                            Username
                        </label>
                    </div>
                    <div className="relative z-0 w-full group">
                        <input
                            type="password"
                            id="login-password"
                            className="block py-2.5 px-2 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                            placeholder=" "
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        <label
                            htmlFor="login-password"
                            className="absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6 peer-focus:text-blue-600"
                        >
                            Password
                        </label>
                    </div>
                    <button
                        type="submit"
                        className="w-full text-white bg-blue-500 hover:bg-blue-600 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 my-1.5 text-center"
                    >
                        Login
                    </button>
                    <p className="text-sm text-gray-500 text-center mt-4">
                        Need an account? <Link to="/register" className="text-gray-500 underline hover:text-blue-600">SIGN UP</Link>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default Login;