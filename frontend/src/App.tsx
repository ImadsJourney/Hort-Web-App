import { useEffect, useState } from "react";
import "./App.css";
import {
  createGroup,
  createChild,
  getChildren,
  getGroups,
  login,
  register,
  updateAttendance,
  updateNotes,
} from "./api";
import type { AttendanceStatus, ChildResponse, HortGroupResponse } from "./types";

function formatAttendanceStatus(status: AttendanceStatus) {
  if (status === "PRESENT") return "Anwesend";
  if (status === "LATE") return "Verspätet";
  if (status === "ABSENT") return "Abwesend";
  if (status === "NOT_RECORDED") return "Nicht erfasst";

  return status;
}

function App() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [token, setToken] = useState<string | null>(null);
  const [groups, setGroups] = useState<HortGroupResponse[]>([]);
  const [children, setChildren] = useState<ChildResponse[]>([]);
  const [childSearchTerm, setChildSearchTerm] = useState("");

  const [newGroupName, setNewGroupName] = useState("");
  const [newGroupGradeLevel, setNewGroupGradeLevel] = useState("");
  const [newGroupSupervisorName, setNewGroupSupervisorName] = useState("");

  const [newChildFirstName, setNewChildFirstName] = useState("");
  const [newChildLastName, setNewChildLastName] = useState("");
  const [newChildNotes, setNewChildNotes] = useState("");
  const [newChildGroupId, setNewChildGroupId] = useState("");

  const [editingChildId, setEditingChildId] = useState<number | null>(null);
  const [notesDraft, setNotesDraft] = useState("");

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
      setError("Login fehlgeschlagen. Bitte überprüfe dein Benutzernamen und Passwort");
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
      setError("Registrierung fehlgeschlagen. Passwort muss mindestens 8 Zeichen haben.");
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
      setError("Dashboard konnte nicht geladen werden.");
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
          supervisorName: newGroupSupervisorName,
        },
        token
      );

      setNewGroupName("");
      setNewGroupGradeLevel("");
      setNewGroupSupervisorName("");

      setMessage("Gruppe wurde erfolgreich erstellt.");
      await loadData(token);
    } catch {
      setError("Gruppe konnte nicht erstellt werden.");
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
          hortGroupId: Number(newChildGroupId),
        },
        token
      );

      setNewChildFirstName("");
      setNewChildLastName("");
      setNewChildNotes("");
      setNewChildGroupId("");

      setMessage("Child created successfully.");
      await loadData(token);
    } catch {
      setError("Could not create child.");
    }
  }

  async function handleUpdateAttendance(
    childId: number,
    attendanceStatus: AttendanceStatus
  ) {
    if (!token) {
      return;
    }

    try {
      setError("");
      setMessage("");

      await updateAttendance(childId, attendanceStatus, token);

      setMessage(
        "Anwesenheitstatus von " + childId + " wurde geändert zu " + attendanceStatus
      );

      await loadData(token);
    } catch {
      setError("Anwesenheitsstatus von " + childId + " konnte nicht geändert werden.");
    }
  }

  function handleStartEditingNotes(child: ChildResponse) {
    setEditingChildId(child.id);
    setNotesDraft(child.notes || "");
  }

  function handleCancelEditingNotes() {
    setEditingChildId(null);
    setNotesDraft("");
  }

  async function handleUpdateNotes(childId: number) {
    if (!token) {
      return;
    }

    try {
      setError("");
      setMessage("");

      await updateNotes(childId, notesDraft, token);

      setMessage("Notiz von " + childId + " wurden geändert.");

      setEditingChildId(null);
      setNotesDraft("");

      await loadData(token);
    } catch {
      setError("Notiz von " + childId + " konnten nicht geändert werden.");
    }
  }

  function handleLogout() {
    setToken(null);
    setUsername("");
    setPassword("");
    setGroups([]);
    setChildren([]);
    setChildSearchTerm("");
    setEditingChildId(null);
    setNotesDraft("");
    setMessage("");
    setError("");
  }

  useEffect(() => {
    if (token) {
      loadData(token);
    }
  }, [token]);

  const normalizedChildSearchTerm = childSearchTerm.trim().toLowerCase();

  const filteredChildren = normalizedChildSearchTerm
    ? children.filter((child) => {
      const fullName = `${child.firstName} ${child.lastName}`.toLowerCase();
      const groupName = child.hortGroupName.toLowerCase();

      return (
        fullName.includes(normalizedChildSearchTerm) ||
        groupName.includes(normalizedChildSearchTerm)
      );
    })
    : children;

  if (!token) {
    return (
      <main className="page">
        <section className="card">
          <h1>Hort-Manager</h1>
          <p className="subtitle">Willkommmen zurück! :))</p>

          <div className="form">
            <label>
              Benutzername
              <input
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                placeholder="Lehrer"
              />
            </label>

            <label>
              Password
              <input
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="Min. 8 Zeichen"
              />
            </label>

            <div className="button-row">
              <button type="button" onClick={handleLogin}>
                Einloggen
              </button>

              <button type="button" className="secondary" onClick={handleRegister}>
                Registrierung
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
          <p className="subtitle">Eingeloggt als {username}</p>
        </div>

        <button type="button" className="secondary" onClick={handleLogout}>
          Ausloggen
        </button>
      </section>

      {error && <p className="error">{error}</p>}
      {message && <p className="success">{message}</p>}

      <section className="grid">
        <div className="left-column">
          <div className="card">
            <h2>Gruppen</h2>

            <div className="form">
              <input
                value={newGroupName}
                onChange={(event) => setNewGroupName(event.target.value)}
                placeholder="Gruppenname"
              />

              <input
                value={newGroupGradeLevel}
                onChange={(event) => setNewGroupGradeLevel(event.target.value)}
                placeholder="Klassenstufe"
              />

              <input
                value={newGroupSupervisorName}
                onChange={(event) => setNewGroupSupervisorName(event.target.value)}
                placeholder="Lehrer"
              />

              <button type="button" onClick={handleCreateGroup}>
                Gruppe erstellen
              </button>
            </div>

            {groups.length === 0 ? (
              <p>Keine Gruppen gefunden.</p>
            ) : (
              <ul className="list">
                {groups.map((group) => (
                  <li key={group.id}>
                    <strong>{group.name}</strong>
                    <span>{group.gradeLevel}</span>
                    <span>{group.supervisorName}</span>
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div className="stats-card">
            <span>Kinder insgesamt</span>
            <strong>{children.length}</strong>
          </div>
        </div>

        <div className="card">
          <h2>Kinder</h2>

          <div className="form">
            <input
              value={newChildFirstName}
              onChange={(event) => setNewChildFirstName(event.target.value)}
              placeholder="Vorname"
            />

            <input
              value={newChildLastName}
              onChange={(event) => setNewChildLastName(event.target.value)}
              placeholder="Nachname"
            />

            <select
              value={newChildGroupId}
              onChange={(event) => setNewChildGroupId(event.target.value)}
            >
              <option value="">Gruppe auswählen</option>

              {groups.map((group) => (
                <option key={group.id} value={String(group.id)}>
                  {group.name}
                </option>
              ))}
            </select>

            <textarea
              value={newChildNotes}
              onChange={(event) => setNewChildNotes(event.target.value)}
              placeholder="Notiz"
            />

            <button type="button" onClick={handleCreateChild}>
              Kind hinzufügen
            </button>
          </div>

          <div className="search-box">
            <input
              value={childSearchTerm}
              onChange={(event) => setChildSearchTerm(event.target.value)}
              placeholder="Kind suchen..."
            />


            {childSearchTerm && (
              <span>
                {filteredChildren.length} von {children.length} Kindern gefunden
              </span>
            )}
          </div>

          {children.length === 0 ? (
            <p>Keine Kinder gefunden.</p>
          ) : filteredChildren.length === 0 ? (
            <p>Kein Kind passt zu deiner Suche.</p>
          ) : (
            <ul className="list">
              {filteredChildren.map((child) => (
                <li key={child.id}>
                  <div className="child-info">
                    <strong>
                      {child.lastName}, {child.firstName}
                    </strong>

                    <span> ---- Gruppe: {child.hortGroupName}</span>
                    <span> ---- Status: {formatAttendanceStatus(child.attendanceStatus)}</span>
                  </div>

                  {editingChildId === child.id ? (
                    <div className="notes-editor">
                      <textarea
                        value={notesDraft}
                        onChange={(event) => setNotesDraft(event.target.value)}
                        placeholder="Notiz"
                      />

                      <button type="button" onClick={() => handleUpdateNotes(child.id)}>
                        Notiz speichern
                      </button>

                      <button type="button" onClick={handleCancelEditingNotes}>
                        Abbrechen
                      </button>
                    </div>
                  ) : (
                    <div className="notes-view">
                      <span>Notiz: {child.notes || "Keine Notiz"}</span>

                      <button
                        type="button"
                        onClick={() => handleStartEditingNotes(child)}
                      >
                        Notiz bearbeiten
                      </button>
                    </div>
                  )}

                  <div className="attendance-btns">
                    <button
                      type="button"
                      onClick={() => handleUpdateAttendance(child.id, "PRESENT")}
                    >
                      Anwesend
                    </button>

                    <button
                      type="button"
                      onClick={() => handleUpdateAttendance(child.id, "LATE")}
                    >
                      Verspätet
                    </button>

                    <button
                      type="button"
                      onClick={() => handleUpdateAttendance(child.id, "ABSENT")}
                    >
                      Nicht Anwesend
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      </section>

    </main>
  );
}

export default App;
