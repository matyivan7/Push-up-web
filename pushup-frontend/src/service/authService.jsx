const BASE_URL = "https://push-up-web-production.up.railway.app/push-up";

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
    // const headers = credentials ? {
    //         Authorization: 'Basic ' + btoa(credentials.username + ':' + credentials.password),
    //     }
    //     : {};

    try {
        const response = await fetch(`${BASE_URL}/login`, {
            method: 'GET',
            headers: {
                Authorization: `Basic ${btoa(credentials.username + ':' + credentials.password)}`
            },
            credentials: 'include',
            mode: 'cors'
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

const logout = async () => {
    const response = await fetch(`${BASE_URL}/logout`, {
        method: 'GET',
        credentials: 'include'
    })

    if (response.ok) {
        localStorage.removeItem('auth');
    }
};


export {login, registerHandle, logout, isAuthenticated};