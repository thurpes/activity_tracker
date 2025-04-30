import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Activity, ActivitySearch, PagedResponse } from '../models/activity.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  private apiUrl = `${environment.apiUrl}/activities`;

  constructor(private http: HttpClient) { }

  getActivities(page: number = 0, size: number = 10, sort: string = 'createdAt'): Observable<PagedResponse<Activity>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get<PagedResponse<Activity>>(this.apiUrl, { params });
  }

  getUserActivities(userId: number, page: number = 0, size: number = 10): Observable<PagedResponse<Activity>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PagedResponse<Activity>>(`${this.apiUrl}/user/${userId}`, { params });
  }

  getRecentActivities(): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${this.apiUrl}/recent`);
  }

  searchActivities(searchParams: ActivitySearch): Observable<PagedResponse<Activity>> {
    let params = new HttpParams();

    if (searchParams.page !== undefined) params = params.set('page', searchParams.page.toString());
    if (searchParams.size !== undefined) params = params.set('size', searchParams.size.toString());
    if (searchParams.userId) params = params.set('userId', searchParams.userId.toString());
    if (searchParams.action) params = params.set('action', searchParams.action);
    if (searchParams.startDate) params = params.set('startDate', searchParams.startDate);
    if (searchParams.endDate) params = params.set('endDate', searchParams.endDate);

    return this.http.get<PagedResponse<Activity>>(`${this.apiUrl}/search`, { params });
  }

  logActivity(activity: Activity): Observable<Activity> {
    return this.http.post<Activity>(this.apiUrl, activity);
  }
}