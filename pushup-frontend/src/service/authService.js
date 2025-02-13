const API_URL = "http://localhost:8080/push-up";

const register = async (userData) => {
    const response = await fetch('&{API_URL}/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    });

    if (!response.ok) {
        throw new Error("User already exists");
    }
    return response.json();
};

const login = async (credentials) => {
    const response = await fetch('&{API_URL}/login', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    });

    if (!response.ok) {
        throw new Error("Login failed, please try again");
    }
    return response.json();
};

export { login, register };
