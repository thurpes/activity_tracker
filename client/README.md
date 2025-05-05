# Activity Tracker Angular Frontend

This is the frontend application for the Real-Time User Activity Tracker system, built with Angular. It provides a user interface for viewing and tracking user activities in real-time using WebSockets.

## Features

- Real-time activity updates using WebSockets
- JWT-based authentication
- Activity search with debouncing (300ms)
- Responsive design with Bootstrap
- State management with NgRx

## Technologies Used

- Angular 16
- NgRx for state management
- RxJS for reactive programming
- Bootstrap for UI components
- SockJS/STOMP for WebSocket communication

## Prerequisites

- Node.js (v14 or later)
- npm or yarn
- Backend API running (on http://localhost:8080 by default)

## Getting Started

1. Clone the repository
```bash
git clone https://github.com/yourusername/activity-tracker-angular.git
cd activity-tracker-angular
```

2. Install dependencies
```bash
npm install
# or
yarn install
```

3. Start the development server
```bash
ng serve
```

4. Open [http://localhost:4200](http://localhost:4200) in your browser

## Project Structure

```
src/
├── app/
│   ├── components/              # Angular components
│   │   ├── activity-list/       # Displays list of activities
│   │   ├── activity-search/     # Search form with debounce
│   │   ├── dashboard/           # Main dashboard page
│   │   └── login/               # Login page
│   ├── core/                    # Core functionality
│   │   ├── auth/                # Authentication services and guards
│   │   ├── services/            # API services
│   │   └── models/              # TypeScript interfaces
│   ├── shared/                  # Shared components and directives
│   │   ├── components/          # Reusable components
│   │   └── directives/          # Custom directives
│   ├── store/                   # NgRx state management
│   │   ├── actions/             # Action definitions
│   │   ├── effects/             # Side effects
│   │   ├── reducers/            # State reducers
│   │   └── selectors/           # State selectors
│   ├── app-routing.module.ts    # Routing configuration
│   └── app.module.ts            # Main Angular module
├── assets/                      # Static assets
├── environments/                # Environment configurations
└── styles.scss                  # Global styles
```

## Key Features Implementation

### Real-time Updates with WebSockets

The application uses WebSocket to receive real-time activity updates:

1. **WebSocketService**: Manages the WebSocket connection lifecycle
2. **SockJS/STOMP**: Provides the WebSocket client implementation
3. **NgRx Integration**: Updates the store when new activities arrive

### Authentication Flow

The application implements a JWT-based authentication flow:

1. **Login Component**: Captures user credentials
2. **TokenStorage Service**: Manages token storage in localStorage
3. **HTTP Interceptor**: Automatically attaches the token to API requests
4. **AuthGuard**: Protects routes from unauthorized access

### Debounced Search

The search functionality uses debouncing to optimize API calls:

1. **ActivitySearchComponent**: Provides the search form UI
2. **RxJS debounceTime**: Delays API calls until the user stops typing (300ms)
3. **NgRx Effects**: Handles asynchronous search requests

## Available Scripts

- `ng serve` - Runs the app in development mode
- `ng test` - Launches the test runner
- `ng build` - Builds the app for production
- `ng lint` - Runs the linter

## Additional Notes

- The application communicates with the backend API at http://localhost:8080 
- Default login credentials: username: "admin", password: "john123" (if using the provided backend)
- WebSocket connection is automatically managed based on authentication state

## Future Improvements

- Implement comprehensive unit and integration tests
- Add dark mode support
- Implement activity export functionality (CSV, PDF)
- Add activity filtering by date range
- Support for different user roles and permissions
- Implement internationalization (i18n)
