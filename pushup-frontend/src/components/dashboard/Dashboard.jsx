import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getPushUpSessionOverview} from "../../service/pushUpService";
import {isAuthenticated, logout} from "../../service/authService";
import "./dashboard.css";

const Dashboard = () => {

    const navigate = useNavigate();
    const [sessionData, setSessionData] = useState([]);
    const [error, setError] = useState(null);
    const [user, setUser] = useState(null);

    useEffect(() => {
        const checkAuthentication = () => {
            const authenticatedUser = isAuthenticated();

            if (!authenticatedUser) {
                navigate("/login");
            } else {
                setUser(authenticatedUser);
            }
        };

        checkAuthentication();
    }, [navigate]);

    useEffect(() => {
        const fetchSessions = async () => {
            try {
                const sessions = await getPushUpSessionOverview();
                setSessionData(sessions);
            } catch (error) {
                setError(error.message);
            }
        };

        fetchSessions();
    }, []);

    const userSessions = sessionData?.userPushUpSessionModels || [];
    const allSessions = sessionData?.allPushUpSessionModels || [];

    const createPushUp = async (e) => {
        e.preventDefault();
        navigate('/new-session');
    }

    const handleLogout = async () => {
        await logout();
        navigate("/login");
    };

    return (
        <div className="dashboard">
            <div className="sessions-box">
                <h1>Your Sessions</h1>
                <div className="session-container">
                    {userSessions.length > 0 ? (
                        userSessions.map((session, index) => (
                            <div key={index} className="session-card">
                                <p><strong>PushUp Count:</strong> {session.pushUpCount}</p>
                                <p><strong>Comment:</strong> {session.comment}</p>
                                <p><strong>Timestamp:</strong> {new Date(session.timeStamp).toLocaleString()}</p>
                            </div>
                        ))
                    ) : (
                        <p>No sessions available.</p>
                    )}
                </div>

                <h1>All Sessions</h1>
                <div className="session-container">
                    {allSessions.length > 0 ? (
                        allSessions.map((session, index) => (
                            <div key={index} className="session-card">
                                <p><strong>User:</strong> {session.user.username}</p>
                                <p><strong>PushUp Count:</strong> {session.pushUpCount}</p>
                                <p><strong>Comment:</strong> {session.comment}</p>
                                <p><strong>Timestamp:</strong> {new Date(session.timeStamp).toLocaleString()}</p>
                            </div>
                        ))
                    ) : (
                        <p>No sessions available.</p>
                    )}
                </div>
            {error && <p className="error-message">{error}</p>}
        </div>
            <button onClick={createPushUp} className="create-session-btn">New Push-up Session</button>
            <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
    );
}

export default Dashboard;