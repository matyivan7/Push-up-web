import React from "react";
import { useNavigate } from "react-router-dom";
import "./navbar.css";

const Navbar = ({ user, onViewChange, onLogout }) => {

    const navigate = useNavigate();

    const handleNewSessionClick = () => {
        navigate("/new-session");
    };

    return (
        <nav className="navbar">
            <div className="navbar-left">
        <span className="navbar-brand">
          {user?.username || "User"}
        </span>
            </div>

            <div className="navbar-center">
                <button onClick={() => onViewChange("all")}>Sessions</button>
                <button onClick={() => onViewChange("my")}>My Sessions</button>
                <button onClick={handleNewSessionClick}>New Session</button>
            </div>

            <div className="navbar-right">
                <button className="logout-btn" onClick={onLogout}>Logout</button>
            </div>
        </nav>
    );
};

export default Navbar;
