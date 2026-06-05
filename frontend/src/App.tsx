import { useEffect, useState } from "react";
import "./App.css";
import {
  createGroup,
  createChild,
  getChildren,
  getGroups,
  login,
  register,
} from "./api";
import type { ChildResponse, HortGroupResponse } from "./types";

function App() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [token, setToken] = useState<string | null>(null);
  const [groups, setGroups] = useState<HortGroupResponse[]>([]);
  const [children, setChildren] = useState<ChildResponse[]>([]);

  const [newGroupName, setNewGroupName] = useState("");
  const [newGroupGradeLevel, setNewGroupGradeLevel] = useState("");
  const [newGroupSupervisorName, setNewGroupSupervisorName] = useState("");

  const [newChildFirstName, setNewChildFirstName] = useState("");
  const [newChildLastName, setNewChildLastName] = useState("");
  const [newChildNotes, setNewChildNotes] = useState("");
  const [newChildGroupId, setNewChildGroupId] = useState("");

  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  async function handleLogin() {
    try {
      setError("");
      setMessage("");

      const response = await login(username, password);

      setToken(response.token);
      setMessage(response.message);
    } catch {
      setError("Login failed. Please check your username and password.");
    }
  }

  async function handleRegister() {
    try {
      setError("");
      setMessage("");

      const response = await register(username, password);

      setToken(response.token);
      setMessage(response.message);
    } catch {
      setError("Registration failed. Username may already exist.");
    }
  }

  async function loadData(currentToken: string) {
    try {
      setError("");

      const loadedGroups = await getGroups(currentToken);
      const loadedChildren = await getChildren(currentToken);

      setGroups(loadedGroups);
      setChildren(loadedChildren);
    } catch {
      setError("Could not load dashboard data.");
    }
  }

  async function handleCreateGroup() {
    if (!token) {
      return;
    }

    try {
      setError("");
      setMessage("");

      await createGroup(
        {
          name: newGroupName,
          gradeLevel: newGroupGradeLevel,
          supervisorName: newGroupSupervisorName
        }, token
      );

      setNewGroupName("");
      setNewGroupGradeLevel("");
      setNewGroupSupervisorName("");

      await loadData(token);
    } catch {
      setError("Could not create group.");
    }
  }

  async function handleCreateChild() {
    if (!token) {
      return;
    }

    try {
      setError("");
      setMessage("");

      await createChild(
        {
          firstName: newChildFirstName,
          lastName: newChildLastName,
          notes: newChildNotes,
          hortGroupId: Number(newChildGroupId)
        }, token
      );

      setNewChildFirstName("");
      setNewChildLastName("");
      setNewChildNotes("");
      setNewChildGroupId("");

      setMessage("Child created succesfully.");

      await loadData(token);

    } catch {
      setError("Could not create child.")
    }
  }

  function handleLogout() {
    setToken(null);
    setUsername("");
    setPassword("");
    setGroups([]);
    setChildren([]);
    setMessage("");
    setError("");
  }

  useEffect(() => {
    if (token) {
      loadData(token);
    }
  }, [token]);

  if (!token) {
    return (
      <main className="page">
        <section className="card">
          <h1>Hort-Manager</h1>
          <p className="subtitle">Willkommmen zurück! :)</p>

          <div className="form">
            <label>
              Username
              <input
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                placeholder="teacher1"
              />
            </label>

            <label>
              Password
              <input
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="password123"
              />
            </label>

            <div className="button-row">
              <button onClick={handleLogin}>Login</button>
              <button className="secondary" onClick={handleRegister}>
                Register
              </button>
            </div>

            {error && <p className="error">{error}</p>}
            {message && <p className="success">{message}</p>}
          </div>
        </section>
      </main>
    );
  }

  return (
    <main className="page">
      <section className="dashboard-header">
        <div>
          <h1>Hort Manager</h1>
          <p className="subtitle">Logged in as {username}</p>
        </div>

        <button className="secondary" onClick={handleLogout}>
          Logout
        </button>
      </section>

      {error && <p className="error">{error}</p>}
      {message && <p className="success">{message}</p>}

      <section className="grid">
        <div className="card">
          <h2>Groups</h2>

          <div>
            <input
              value={newGroupName}
              onChange={(event) => setNewGroupName(event.target.value)}
              placeholder="Group name"
            />

            <input
              value={newGroupGradeLevel}
              onChange={(event) => setNewGroupGradeLevel(event.target.value)}
              placeholder="Grade level"
            />

            <input
              value={newGroupSupervisorName}
              onChange={(event) => setNewGroupSupervisorName(event.target.value)}
              placeholder="Supervisor name"
            />

            <button onClick={handleCreateGroup}>Create group</button>
          </div>

          {groups.length === 0 ? (
            <p>No groups found.</p>
          ) : (
            <>
              <ul className="list">
                {groups.map((group) => (
                  <li key={group.id}>
                    <strong>{group.name}</strong>
                    <span> - </span>
                    <span>{group.gradeLevel} - </span>
                    <span>{group.supervisorName}</span>
                  </li>
                ))}
              </ul>
            </>
          )}
        </div>

        <div className="card">
          <h2>Children</h2>

          <div className="form">
            <input
              value={newChildFirstName}
              onChange={(event) => setNewChildFirstName(event.target.value)}
              placeholder="First name"
            />

            <input
              value={newChildLastName}
              onChange={(event) => setNewChildLastName(event.target.value)}
              placeholder="Last name"
            />

            <select
              value={newChildGroupId}
              onChange={(event => setNewChildGroupId(event.target.value))}
            >
              <option value="">Select group</option>

              {groups.map((group) => (
                <option key={group.id} value={String(group.id)}>
                  {group.name}
                </option>
              ))}
            </select>

            <button onClick={handleCreateChild}>Create child</button>

          </div>

          {children.length === 0 ? (
            <p>No children found.</p>
          ) : (
            <ul className="list">
              {children.map((child) => (
                <li key={child.id}>
                  <strong>
                    {child.firstName} {child.lastName}
                  </strong>
                  <span>Group: {child.hortGroupName}</span>
                  <span>Status: {child.attendanceStatus}</span>
                  <span>Notes: {child.notes || "No notes"}</span>
                </li>
              ))}
            </ul>
          )}
        </div>
      </section>
    </main >
  );
}

export default App;
