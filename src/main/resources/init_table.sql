
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `occupation` varchar(50) DEFAULT '' NOT NULL,
  `avatar` varchar(512) DEFAULT '' NOT NULL COMMENT 'the address of user avatar',
  `email` varchar(128) NOT NULL,
  `password` varchar(60) NOT NULL COMMENT 'save user encrypted password',
  `gender` tinyint unsigned NOT NULL COMMENT '0 - male / 1 - female / 2 - others',
  `status` tinyint unsigned DEFAULT 0 NOT NULL COMMENT '0 - valid / 1 - fraud',
  `description` varchar(300) NOT NULL,
  `birthday` date DEFAULT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- insert into test user data.
insert into user
(`username`, `occupation`, `avatar`, `email`, `password`, `gender`, `status`, `description`, `birthday`)
value
('Alen', 'programmer', 'https://xxx.com/xx.png', 'alen@meet5.com', 'xxx', 0, 0, 'test user 1', '1991-01-01'),
('Bob', 'manager', 'https://xxx.com/xx.png', 'bob@meet5.com', 'xxx', 1, 0, 'test user 2', '1994-02-01');

DROP TABLE IF EXISTS `user_action_record`;

CREATE TABLE `user_action_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `from_id` int NOT NULL,
  `to_id` int NOT NULL,
  `type` tinyint unsigned NOT NULL COMMENT '0 - visit / 1 - like',
  `recorded_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_to_id_updated_at` (`to_id`, `recorded_at`),
  KEY `idx_from_id_updated_at` (`from_id`, `recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;