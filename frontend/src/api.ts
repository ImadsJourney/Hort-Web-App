import type { AuthResponse, CreateChildRequest, CreateHortGroupRequest, HortGroupResponse, AttendanceStatus, ChildResponse } from "./types";

const API_URL = "http://localhost:8080"; //Variable, damit man nicht immer wieder die URL eingeben muss in Zukunft

/**
 *
 * export = ist in anderen Files nutzbar
 * async, weil es Zeit brauch bis die Anfrage bearbeitet wird im Backend
 * eine Konstante response, die mit await extra wartet und mit fetch die HTTP-Anfrage an das Backend sendet
 * fetch besteht aus (URL, OPTIONS)
 * OPTIONS beschreiben welche Http-Methode, Header -> Information für Spring im Backend
 * Und weil Java kein TS Objekt benötigt, machen wir es zu einem JSON String
 * Dann wird geprüft ob der Http Status OK ist
 * Die Antwort wird wieder zu einem JS/TS Objekt gemacht
 *
 */
export async function login(username: string, password: string): Promise<AuthResponse> {
  const response = await fetch(`${API_URL}/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password })
  });

  if (!response.ok) {
    throw new Error("Login failed");
  }

  return response.json();
}

export async function register(username: string, password: string): Promise<AuthResponse> {
  const response = await fetch(`${API_URL}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ username: username, password: password })
  });

  if (!response.ok) {
    throw new Error("Login failed");
  }

  return response.json();
}

export async function getGroups(token: string): Promise<HortGroupResponse[]> {
  const response = await fetch(`${API_URL}/groups`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token.trim()}`
    }
  });


  if (!response.ok) {
    throw new Error("Login failed");
  }

  return response.json();
}

export async function getChildren(token: string): Promise<ChildResponse[]> {
  const response = await fetch(`${API_URL}/children`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token.trim()}`,
    },
  });

  if (!response.ok) {
    throw new Error("Could not load children");
  }

  return response.json();
}

export async function getChildrenByGroup(token: string, groupId: number): Promise<ChildResponse> {
  const response = await fetch(`${API_URL}/groups/${groupId}/children`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw new Error("Login failed");
  }

  return response.json();
}

export async function createGroup(
  group: CreateHortGroupRequest,
  token: string,
): Promise<HortGroupResponse> {
  const response = await fetch(`${API_URL}/groups`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(group),
  });

  if (!response.ok) {
    throw new Error("Could not create group");
  }

  return response.json();
}

export async function createChild(
  child: CreateChildRequest,
  token: string,
): Promise<ChildResponse> {
  const response = await fetch(`${API_URL}/children`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(child),
  });

  if (!response.ok) {
    throw new Error("Could not create child");
  }

  return response.json();
}

export async function updateAttendance(
  childId: number,
  attendanceStatus: AttendanceStatus,
  token: string,
): Promise<ChildResponse> {
  const response = await fetch(`${API_URL}/children/${childId}/attendance`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ attendanceStatus }),
  });

  if (!response.ok) {
    throw new Error("Could not update attendance");
  }

  return response.json();
}

export async function updateNotes(
  childId: number,
  notes: string,
  token: string,
): Promise<ChildResponse> {
  const response = await fetch(`${API_URL}/children/${childId}/notes`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ notes }),
  });

  if (!response.ok) {
    throw new Error("Could not update notes");
  }

  return response.json();
}

