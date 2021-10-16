export interface Login {
  email: string;
  password: string;
}

export interface Account {
  id?: string;
  username: string;
  email: string;
  password?: string;
  status: AccountStatus;
  emailVerified: boolean;
  role: Role;
  createdAt?: string;
}

export enum Role {
  ROOT_ADMIN = 'root_admin',
  AGENCY_USER = 'agency_user',
  USER = 'user',
}

export enum AccountStatus {
  ACTIVE = 'active',
  INACTIVE = 'inactive',
}

