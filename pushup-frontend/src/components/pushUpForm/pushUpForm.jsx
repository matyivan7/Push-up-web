import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createPushUp } from "../../service/pushUpService";
import "./pushUpForm.css";

const PushUpForm = () => {

    const navigate = useNavigate();
    const [pushUpCount, setPushUpCount] = useState("");
    const [comment, setComment] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!pushUpCount || !comment) {
            setError("Please fill in all the fields");
            return;
        }

        const pushUpSessionCreationModel = {
            pushUpCount: parseInt(pushUpCount),
            comment: comment,
            timeStamp: new Date().toISOString(),
        };

        try {
            await createPushUp(pushUpSessionCreationModel);
            navigate("/dashboard");
        } catch (err) {
            setError("Failed to create push-up session. Please try again.");
        }
    };

    return (
        <div className="push-up-form">
            <h1>New Push-Up Session</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="pushUpCount">Push-Up Count</label>
                    <input
                        type="number"
                        id="pushUpCount"
                        value={pushUpCount}
                        onChange={(e) => setPushUpCount(e.target.value)}
                        placeholder="Enter push-up count"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="comment">Comment</label>
                    <textarea
                        id="comment"
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                        placeholder="Enter comment"
                    />
                </div>

                {error && <p className="error-message">{error}</p>}

                <button onClick={navigate("/dashboard")} className="logout-btn">Back</button>
                <button type="submit" className="submit-btn">Submit</button>
            </form>
        </div>
    );
};

export default PushUpForm;