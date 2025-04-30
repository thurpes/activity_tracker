import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { Client, IMessage, IFrame, Stomp } from '@stomp/stompjs';
import { TokenStorage } from '../auth/token.storage';
import { Activity } from '../models/activity.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: Client | null = null;
  private activitySubject = new Subject<Activity>();
  private connected = false;

  constructor(private tokenStorage: TokenStorage) { }

  connect(): Observable<boolean> {
    const connectedSubject = new Subject<boolean>();

    // Create a new STOMP client
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(`${environment.apiUrl}/ws`),
      connectHeaders: {
        'Authorization': `Bearer ${this.tokenStorage.getToken()}`
      },
      debug: function(str) {
        // Disable debug logs in production
        if (!environment.production) {
          console.log(str);
        }
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    // Define connect callback
    this.stompClient.onConnect = (frame: IFrame) => {
      this.connected = true;
      this.subscribeToActivities();
      connectedSubject.next(true);
      connectedSubject.complete();
    };

    // Define error callback
    this.stompClient.onStompError = (frame: IFrame) => {
      console.error('STOMP error:', frame.headers['message']);
      this.connected = false;
      connectedSubject.next(false);
      connectedSubject.complete();
    };

    // Start the connection
    this.stompClient.activate();

    return connectedSubject.asObservable();
  }

  disconnect(): void {
    if (this.stompClient && this.connected) {
      this.stompClient.deactivate();
      this.connected = false;
    }
  }

  private subscribeToActivities(): void {
    if (!this.connected || !this.stompClient) {
      console.warn('WebSocket not connected. Cannot subscribe.');
      return;
    }

    this.stompClient.subscribe('/topic/activities', (message: IMessage) => {
      const activity: Activity = JSON.parse(message.body);
      this.activitySubject.next(activity);
    });
  }

  onActivity(): Observable<Activity> {
    return this.activitySubject.asObservable();
  }

  isConnected(): boolean {
    return this.connected;
  }
}