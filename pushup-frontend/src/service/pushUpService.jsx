const API_URL = "https://push-up-web-production.up.railway.app/push-up";
// const API_URL = "http://localhost:8080/push-up";

const createPushUp = async (pushUpSessionCreationModel) => {
    const token = localStorage.getItem("jwt");
    const response = await fetch(`${API_URL}/new-session`, {
        method: 'POST',
        headers: {
            "Authorization": `Bearer ${token}`,
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
    const token = localStorage.getItem("jwt");
    console.log("getPushUpSessionOverview: ", token);
    const response = await fetch(`${API_URL}`, {
        method: 'GET',
        headers: {
            "Authorization": `Bearer ${token}`,
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