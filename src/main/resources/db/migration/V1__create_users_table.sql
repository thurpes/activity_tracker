-- V1__create_users_table.sql
-- Creates the users table and adds a default admin user

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Add table comments
COMMENT ON TABLE users IS 'Stores user account information';

-- Add column comments
COMMENT ON COLUMN users.id IS 'Primary key';
COMMENT ON COLUMN users.username IS 'Unique username for login';
COMMENT ON COLUMN users.password IS 'Encrypted password (BCrypt)';
COMMENT ON COLUMN users.email IS 'User email address (unique)';
COMMENT ON COLUMN users.first_name IS 'User first name';
COMMENT ON COLUMN users.last_name IS 'User last name';
COMMENT ON COLUMN users.created_at IS 'Timestamp when user was created';
COMMENT ON COLUMN users.updated_at IS 'Timestamp of last update to user record';

-- Insert a default admin user (password: john123)
-- The password hash is for 'john123' using BCrypt
INSERT INTO users (username, password, email, first_name, last_name)
VALUES ('admin', 'john123$2a$10$qQhTEOb88Se962ExsWlIa.IGDXtYvwrWZQ1qx.j.f4HRN1cBmZWRC', 'admin@example.com', 'Admin', 'User');