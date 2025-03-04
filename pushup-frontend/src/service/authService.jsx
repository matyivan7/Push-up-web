const BASE_URL = "http://localhost:8080/push-up";

const registerHandle = async (userData) => {
    const response = await fetch(`${BASE_URL}/register`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'include',
        body: JSON.stringify(userData)
    });

    if (!response.ok) {
        throw new Error("User already exists");
    }
    return response.json();
};

const login = async (credentials) => {
    const headers = credentials ? {
            Authorization: 'Basic ' + btoa(credentials.username + ':' + credentials.password),
        }
        : {};

    try {
        const response = await fetch(`${BASE_URL}/login`, {
            method: 'GET',
            headers: headers,
            credentials: 'include'
        });

        if (response.ok) {
            const userDetails = await response.json();
            localStorage.setItem('auth', JSON.stringify(userDetails));
            return userDetails;
        } else {
            throw new Error('Unauthorized');
        }
    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
};

const isAuthenticated = () => {
    const user = localStorage.getItem('auth');
    return user ? JSON.parse(user) : null;
};

const logout = async (credentials) => {
    const response = await fetch(`${BASE_URL}/logout`, {
        method: 'GET',
        credentials: 'include'
    })

    if (response.ok) {
        localStorage.removeItem('auth');
    }
};

const getAuthStatus = () => {
    const user = isAuthenticated();
    return user !== null;
};

export {login, registerHandle, logout, isAuthenticated, getAuthStatus};