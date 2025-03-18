import React from "react";
import { useNavigate } from "react-router-dom";

const Navbar = ({ user, onViewChange, onLogout }) => {

    const navigate = useNavigate();

    const handleNewSessionClick = () => {
        navigate("/new-session");
    };

    return (
        <nav className="bg-white dark:bg-gray-900 fixed w-full z-20 top-0 start-0 border-b border-gray-200 dark:border-gray-600">
            <div className="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-4">
                <span className="text-2xl font-semibold whitespace-nowrap dark:text-white">
                    {user?.username || "Guest"}
                </span>

                <div className="items-center justify-center hidden w-full md:flex md:w-auto md:order-1">
                    <ul className="flex flex-col p-4 md:p-0 mt-4 font-medium border border-gray-100 rounded-lg bg-gray-50 md:space-x-8 rtl:space-x-reverse md:flex-row md:mt-0 md:border-0 md:bg-white dark:bg-gray-800 md:dark:bg-gray-900 dark:border-gray-700">
                        <li>
                            <button onClick={() => onViewChange("all")} className="text-gray-900 hover:text-blue-700 dark:text-white">Sessions</button>
                        </li>
                        <li>
                            <button onClick={() => onViewChange("my")} className="text-gray-900 hover:text-blue-700 dark:text-white">My Sessions</button>
                        </li>
                        <li>
                            <button onClick={handleNewSessionClick} className="text-gray-900 hover:text-blue-700 dark:text-white">New Session</button>
                        </li>
                    </ul>
                </div>

                <div className="flex md:order-2">
                    <button onClick={onLogout} className="text-white bg-red-500 hover:bg-red-600 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm px-4 py-2 text-center dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-800">
                        Logout
                    </button>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
