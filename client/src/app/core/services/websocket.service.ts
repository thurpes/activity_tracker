import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { TokenStorage } from '../auth/token.storage';
import { Activity } from '../models/activity.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: any;
  private activitySubject = new Subject<Activity>();
  private connected = false;
  private subscriptions = new Map();

  constructor(private tokenStorage: TokenStorage) { }

  connect(): Observable<boolean> {
    const connectedSubject = new Subject<boolean>();
    const socket = new SockJS(`${environment.apiUrl}/ws`);
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null; // Disable console logging

    const token = this.tokenStorage.getToken();
    const headers = {
      'Authorization': `Bearer ${token}`
    };

    this.stompClient.connect(
      headers,
      () => {
        this.connected = true;
        this.subscribeToActivities();
        connectedSubject.next(true);
        connectedSubject.complete();
      },
      (error: any) => {
        console.error('WebSocket connection error:', error);
        this.connected = false;
        connectedSubject.next(false);
        connectedSubject.complete();

        // Attempt to reconnect after 5 seconds
        setTimeout(() => {
          this.connect();
        }, 5000);
      }
    );

    return connectedSubject.asObservable();
  }

  disconnect(): void {
    if (this.stompClient && this.connected) {
      this.unsubscribeFromActivities();
      this.stompClient.disconnect();
      this.connected = false;
    }
  }

  private subscribeToActivities(): void {
    if (!this.connected) {
      console.warn('WebSocket not connected. Cannot subscribe.');
      return;
    }

    const subscription = this.stompClient.subscribe('/topic/activities', (message: any) => {
      const activity: Activity = JSON.parse(message.body);
      this.activitySubject.next(activity);
    });

    this.subscriptions.set('/topic/activities', subscription);
  }

  private unsubscribeFromActivities(): void {
    if (this.subscriptions.has('/topic/activities')) {
      const subscription = this.subscriptions.get('/topic/activities');
      subscription.unsubscribe();
      this.subscriptions.delete('/topic/activities');
    }
  }

  onActivity(): Observable<Activity> {
    return this.activitySubject.asObservable();
  }

  isConnected(): boolean {
    return this.connected;
  }
}