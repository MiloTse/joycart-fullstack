-- Add google_id column for Google login
-- Task B8: Add google_id to users table

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS google_id VARCHAR(255);

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_google_id
    ON users (google_id);

COMMENT ON COLUMN users.google_id IS 'Google user unique identifier (sub)';
