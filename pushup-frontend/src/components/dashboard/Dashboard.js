import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getPushUpSessionOverview} from "../../service/authService";
import "./dashboard.css";

const Dashboard = () => {

    const navigate = useNavigate();
    const [error, setError] = useState('');
    const [userSessions, setUserSessions] = useState([]);
    const [allSessions, setAllSessions] = useState([]);

    useEffect(() => {
        const fetchSessionOverview = async () => {
            try {
                const data = await getPushUpSessionOverview();
                setUserSessions(data.userPushUpSessionModels);
                setAllSessions(data.allPushUpSessionModels);
            } catch (err) {
                setError('An error occurred while fetching data');
            }
        };

        fetchSessionOverview();
    }, []);

    const createPushUp = async (e) => {
        e.preventDefault();
        navigate('/new-session');
    }

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
        </div>
    );
}

export default Dashboard;