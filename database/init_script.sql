CREATE TABLE `user`
(
    `user_id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username`        VARCHAR(50)                                                    NOT NULL,
    `hashed_password` VARCHAR(255)                                                   NOT NULL,
    `email`           VARCHAR(50)                                                    NOT NULL,
    `first_name`      VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL,
    `last_name`       VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL,
    `profile_picture` VARCHAR(255)                                                   NULL,
    `created_at`      DATETIME DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `last_updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `is_enabled`      BOOLEAN  DEFAULT FALSE                                         NOT NULL,
    CONSTRAINT `username_unique` UNIQUE (`username`),
    CONSTRAINT `email_unique` UNIQUE (`email`)
);

CREATE TABLE `permission`
(
    `permission_id`   BIGINT AUTO_INCREMENT PRIMARY KEY,
    `permission_name` VARCHAR(255)                                                  NOT NULL,
    `permission_desc` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
    CONSTRAINT `permission_name_unique` UNIQUE (`permission_name`)
);

CREATE TABLE `role`
(
    `role_id`   BIGINT AUTO_INCREMENT PRIMARY KEY,
    `role_name` VARCHAR(255)                                                  NOT NULL,
    `role_desc` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
    CONSTRAINT `role_name_unique` UNIQUE (`role_name`)
);

CREATE TABLE `user_role`
(
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    FOREIGN KEY (`user_id`) REFERENCES user (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES role (`role_id`) ON DELETE CASCADE
);

CREATE TABLE `role_permission`
(
    `role_id`       BIGINT NOT NULL,
    `permission_id` BIGINT NOT NULL,
    PRIMARY KEY (`role_id`, `permission_id`),
    FOREIGN KEY (`role_id`) REFERENCES `role` (role_id) ON DELETE CASCADE,
    FOREIGN KEY (`permission_id`) REFERENCES `permission` (`permission_id`) ON DELETE CASCADE
);

CREATE TABLE `token`
(
    `token_id`    BIGINT AUTO_INCREMENT PRIMARY KEY,
    `token_value` VARCHAR(255)               NOT NULL,
    `token_type`  ENUM ('BEARER', 'CONFIRM') NOT NULL,
    `user_id`     BIGINT                     NOT NULL,
    `is_revoked`  BOOLEAN                    NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES user (`user_id`) ON DELETE CASCADE
);