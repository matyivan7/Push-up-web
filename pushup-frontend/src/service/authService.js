const API_URL = "http://localhost:8080/push-up";

const registerHandle = async (userData) => {
    const response = await fetch(`${API_URL}/register`, {
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
    const response = await fetch(`${API_URL}/login`, {
        method: 'POST',
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

const createPushUp = async (pushUpSessionCreationModel) => {
    const response = await fetch(`${API_URL}/new-session`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(pushUpSessionCreationModel)
    });

    if (!response.ok) {
        throw new Error("Create PushUp session failed, please try again");
    }
    return response.json();
}

const getPushUpSessionOverview = async () => {
    const response = await fetch(`${API_URL}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error("Failed to fetch session data");
    }
    return response.json();
};

export {login, registerHandle, createPushUp, getPushUpSessionOverview};