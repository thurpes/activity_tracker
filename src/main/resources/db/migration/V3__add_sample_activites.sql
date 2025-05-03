-- Get the admin user's ID
DO $$
DECLARE
    admin_id BIGINT;
BEGIN
    SELECT id INTO admin_id FROM users WHERE username = 'admin';

    -- Insert sample activities for admin user
    INSERT INTO activities (user_id, action, description, ip_address, user_agent, created_at)
    VALUES
        (admin_id, 'LOGIN', 'Initial login after account creation', '192.168.1.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', NOW() - INTERVAL '30 minutes'),
        (admin_id, 'VIEW_DASHBOARD', 'Viewed admin dashboard', '192.168.1.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', NOW() - INTERVAL '25 minutes'),
        (admin_id, 'UPDATE_PROFILE', 'Updated profile information', '192.168.1.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', NOW() - INTERVAL '20 minutes'),
        (admin_id, 'VIEW_USERS', 'Viewed user management page', '192.168.1.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', NOW() - INTERVAL '15 minutes'),
        (admin_id, 'SYSTEM_CONFIG', 'Changed system configuration settings', '192.168.1.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', NOW() - INTERVAL '10 minutes'),
        (admin_id, 'LOGOUT', 'User logout', '192.168.1.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', NOW() - INTERVAL '5 minutes');

    -- Create a test user
    INSERT INTO users (username, password, email, first_name, last_name)
    VALUES ('testuser', '$2a$10$7tYpkLKBXGgCVqO5HiDkSu4qnI3oe8XM8WKVnwHimPFAdJ2xm5CHy', 'test@example.com', 'Test', 'User');
    
    -- Get the test user's ID
    DECLARE
        test_id BIGINT;
    BEGIN
        SELECT id INTO test_id FROM users WHERE username = 'testuser';
        
        -- Insert sample activities for test user
        INSERT INTO activities (user_id, action, description, ip_address, user_agent, created_at)
        VALUES
            (test_id, 'REGISTRATION', 'New user registration', '10.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', NOW() - INTERVAL '2 hours'),
            (test_id, 'LOGIN', 'First login after registration', '10.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', NOW() - INTERVAL '1 hour 55 minutes'),
            (test_id, 'UPDATE_PROFILE', 'Completed profile information', '10.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', NOW() - INTERVAL '1 hour 50 minutes'),
            (test_id, 'VIEW_DASHBOARD', 'Viewed main dashboard', '10.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', NOW() - INTERVAL '1 hour 45 minutes'),
            (test_id, 'LOGOUT', 'User logout', '10.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', NOW() - INTERVAL '1 hour 40 minutes');
    END;
END $$;