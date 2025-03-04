import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getPushUpSessionOverview} from "../../service/pushUpService";
import {isAuthenticated, logout} from "../../service/authService";
import Navbar from "../navbar/Navbar";
import "./dashboard.css";

const Dashboard = () => {

    const navigate = useNavigate();
    const [sessionData, setSessionData] = useState([]);
    const [error, setError] = useState(null);
    const [user, setUser] = useState(null);
    const [view, setView] = useState("all");

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

    const handleNewSession = async (e) => {
        e.preventDefault();
        navigate('/new-session');
    }

    const handleLogout = async () => {
        await logout();
        navigate("/login");
    };

    return (
        <div className="dashboard">
            <Navbar
                user={user}
                onViewChange={setView}
                onLogout={handleLogout}
            />

            <div className="sessions-box">
                {view === "my" ? (
                    <>
                        <h1>Your Sessions</h1>
                        <div className="session-container">
                            {userSessions.length > 0 ? (
                                userSessions.map((session, index) => (
                                    <div key={index} className="session-card">
                                        <p><strong>Fekvőtámasz:</strong> {session.pushUpCount}</p>
                                        <p><strong>Komment:</strong> {session.comment}</p>
                                        <p><strong>Dátum:</strong>
                                            {new Date(session.timeStamp).toLocaleString()}
                                        </p>
                                    </div>
                                ))
                            ) : (
                                <p>No sessions available.</p>
                            )}
                        </div>
                    </>
                ) : (
                    <>
                        <h1>All Sessions</h1>
                        <div className="session-container">
                            {allSessions.length > 0 ? (
                                allSessions.map((session, index) => (
                                    <div key={index} className="session-card">
                                        <p><strong>Felhasználó:</strong> {session.user.username}</p>
                                        <p><strong>Fekvőtámasz:</strong> {session.pushUpCount}</p>
                                        <p><strong>Komment:</strong> {session.comment}</p>
                                        <p><strong>Dátum:</strong>
                                            {new Date(session.timeStamp).toLocaleString()}
                                        </p>
                                    </div>
                                ))
                            ) : (
                                <p>No sessions available.</p>
                            )}
                        </div>
                    </>
                )}

                {error && <p className="error-message">{error}</p>}
            </div>
        </div>
    );
};

export default Dashboard;