-- V2__create_activities_table.sql
-- Creates the activities table and related indexes

CREATE TABLE activities (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    action VARCHAR(100) NOT NULL,
    description TEXT,
    ip_address VARCHAR(50),
    user_agent VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create indexes for frequently queried columns
-- Index for user_id to speed up queries filtering by user
CREATE INDEX idx_activities_user_id ON activities(user_id);

-- Index for created_at to optimize time-based searches and sorting
CREATE INDEX idx_activities_created_at ON activities(created_at);

-- Composite index for both user_id and created_at
-- Useful for queries that filter by user and then sort by time
CREATE INDEX idx_activities_user_created ON activities(user_id, created_at DESC);

-- Index for action field to speed up searches by activity type
CREATE INDEX idx_activities_action ON activities(action);

-- Add a comment to the table for documentation
COMMENT ON TABLE activities IS 'Stores user activity records for tracking and auditing purposes';

-- Add comments to columns
COMMENT ON COLUMN activities.id IS 'Primary key';
COMMENT ON COLUMN activities.user_id IS 'Foreign key to users table';
COMMENT ON COLUMN activities.action IS 'Type of action performed (LOGIN, LOGOUT, VIEW, etc.)';
COMMENT ON COLUMN activities.description IS 'Detailed description of the activity';
COMMENT ON COLUMN activities.ip_address IS 'IP address of the client';
COMMENT ON COLUMN activities.user_agent IS 'Browser/client user agent string';
COMMENT ON COLUMN activities.created_at IS 'Timestamp when the activity occurred';