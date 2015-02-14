SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

CREATE TABLE `database_connections` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`name` varchar(200) NOT NULL,
`url` varchar(2048) NOT NULL,
`username` varchar(200) NOT NULL,
`password` varchar(200) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

CREATE TABLE `queries` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`key` varchar(256) NOT NULL,
`database_connection_id` bigint(20) NOT NULL,
PRIMARY KEY (`id`),
KEY `database_connection_id` (`database_connection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

ALTER TABLE  `queries` ADD INDEX  `database_connection_id` (  `database_connection_id` );

-- --------------------------------------------------------

CREATE TABLE `query_versions` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`content` text NOT NULL,
`version` bigint(20) NOT NULL,
`is_current_version` tinyint(4) NOT NULL,
`query_id` bigint(20) NOT NULL,
PRIMARY KEY (`id`),
KEY `query_id` (`query_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

ALTER TABLE  `query_versions` ADD INDEX  `query_id` (  `query_id` );