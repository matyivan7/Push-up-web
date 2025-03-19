const API_URL = "https://push-up-web-production.up.railway.app/push-up";
// const API_URL = "http://localhost:8080/push-up";

const createPushUp = async (pushUpSessionCreationModel) => {
    const response = await fetch(`${API_URL}/new-session`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        mode: 'cors',
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
        credentials: 'include',
        mode: 'cors',
    });

    if (!response.ok) {
        throw new Error("Failed to fetch session data");
    }
    return response.json();
};

export {createPushUp, getPushUpSessionOverview};