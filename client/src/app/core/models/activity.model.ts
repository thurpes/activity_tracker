export interface Activity {
  id?: number;
  userId: number;
  username?: string;
  action: string;
  description?: string;
  ipAddress?: string;
  userAgent?: string;
  createdAt?: string;
}

export interface ActivitySearch {
  userId?: number | null;
  action?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  page?: number;
  size?: number;
}

export interface PagedResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
  };
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  empty: boolean;
  number: number;
  size: number;
}