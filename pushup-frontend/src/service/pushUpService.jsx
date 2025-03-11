const API_URL = "push-up-web.railway.internal";

const createPushUp = async (pushUpSessionCreationModel) => {
    const response = await fetch(`${API_URL}/new-session`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
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
            'Content-Type': 'application/json',
        },
        credentials: 'include'
    });

    if (!response.ok) {
        throw new Error("Failed to fetch session data");
    }
    return response.json();
};

export {createPushUp, getPushUpSessionOverview};