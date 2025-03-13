const API_URL = "https://push-up-web-production.up.railway.app/push-up";
// const API_URL = "http://localhost:8080/push-up";

const registerHandle = async (userData) => {
    const response = await fetch(`${API_URL}/register`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'include',
        mode: 'cors',
        body: JSON.stringify(userData)
    });

    if (!response.ok) {
        throw new Error("User already exists");
    }
    return response.json();
};

const login = async (credentials) => {
    try {
        const response = await fetch(`${API_URL}/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            credentials: 'include',
            mode: 'cors',
            body: JSON.stringify(credentials)
        });

        if (!response.ok) {
            throw new Error('Invalid username or password');
        }

        return response.json();

    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
 };

const logout = async () => {
    const response = await fetch(`${API_URL}/logout`, {
        method: 'POST',
        credentials: 'include'
    });
    if (response.ok) {
        // client-side state
    }
};

export {login, registerHandle, logout};