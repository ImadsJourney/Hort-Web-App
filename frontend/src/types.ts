/*
 *
 * Hier werden die einzelnen Typen definiert, um Übersicht bei den fetch Statements zu verschaffen
 * und dadurch entsteht Modulasierung
 * export -> weil diese in api.ts benutzt werden sollen
 */

export type AuthResponse = {
  userId: number;
  username: string;
  token: string;
  message: string;
}

export type AttendanceStatus = "NOT_RECORDED" | "PRESENT" | "LATE" | "ABSENT";

export type ChildResponse = {
  id: number;
  firstName: string;
  lastName: string;
  attendanceStatus: AttendanceStatus;
  notes: string;
  hortGroupId: number;
  hortGroupName: string;
}

export type HortGroupResponse = {
  id: number;
  name: string;
  gradeLevel: string;
  supervisorName: string;
};

export type CreateHortGroupRequest = {
  name: string;
  gradeLevel: string;
  supervisorName: string;
}

export type CreateChildRequest = {
  firstName: string;
  lastName: string;
  notes: string;
  hortGroupId: number;
}
