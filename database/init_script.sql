CREATE TABLE users
(
    user_id         BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50)                         NOT NULL,
    hashed_password VARCHAR(255)                        NOT NULL,
    email           VARCHAR(50)                         NOT NULL,
    first_name      VARCHAR(50) COLLATE "C"             NOT NULL,
    last_name       VARCHAR(50) COLLATE "C"             NOT NULL,
    profile_picture VARCHAR(255)                        NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_enabled      BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT username_unique UNIQUE (username),
    CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE permissions
(
    permission_id   BIGSERIAL PRIMARY KEY,
    permission_name VARCHAR(255)             NOT NULL,
    permission_desc VARCHAR(255) COLLATE "C" NULL,
    CONSTRAINT permission_name_unique UNIQUE (permission_name)
);

CREATE TABLE roles
(
    role_id   BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(255)             NOT NULL,
    role_desc VARCHAR(255) COLLATE "C" NULL,
    CONSTRAINT role_name_unique UNIQUE (role_name)
);

CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE
);

CREATE TABLE role_permission
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions (permission_id) ON DELETE CASCADE
);

CREATE TABLE tokens
(
    token_id    BIGSERIAL PRIMARY KEY,
    token_value VARCHAR(255) NOT NULL,
    token_type  VARCHAR(10)  NOT NULL CHECK (token_type IN ('BEARER', 'CONFIRM')),
    user_id     BIGINT       NOT NULL,
    is_revoked  BOOLEAN      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
