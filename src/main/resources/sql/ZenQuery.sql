CREATE TABLE database_connections
(
  id INT PRIMARY KEY NOT NULL,
  name VARCHAR NOT NULL,
  url VARCHAR NOT NULL,
  username VARCHAR NOT NULL,
  password VARCHAR NOT NULL
);
CREATE TABLE database_connections1_id_seq
(
  sequence_name VARCHAR NOT NULL,
  last_value BIGINT NOT NULL,
  start_value BIGINT NOT NULL,
  increment_by BIGINT NOT NULL,
  max_value BIGINT NOT NULL,
  min_value BIGINT NOT NULL,
  cache_value BIGINT NOT NULL,
  log_cnt BIGINT NOT NULL,
  is_cycled BOOL NOT NULL,
  is_called BOOL NOT NULL
);
CREATE TABLE queries
(
  id INT PRIMARY KEY NOT NULL,
  key VARCHAR NOT NULL,
  database_connection_id INT NOT NULL
);
CREATE TABLE queries_id_seq
(
  sequence_name VARCHAR NOT NULL,
  last_value BIGINT NOT NULL,
  start_value BIGINT NOT NULL,
  increment_by BIGINT NOT NULL,
  max_value BIGINT NOT NULL,
  min_value BIGINT NOT NULL,
  cache_value BIGINT NOT NULL,
  log_cnt BIGINT NOT NULL,
  is_cycled BOOL NOT NULL,
  is_called BOOL NOT NULL
);
CREATE TABLE query_versions
(
  id INT PRIMARY KEY NOT NULL,
  content VARCHAR NOT NULL,
  version INT NOT NULL,
  is_current_version BOOL NOT NULL,
  query_id INT NOT NULL
);
CREATE TABLE query_versions_id_seq
(
  sequence_name VARCHAR NOT NULL,
  last_value BIGINT NOT NULL,
  start_value BIGINT NOT NULL,
  increment_by BIGINT NOT NULL,
  max_value BIGINT NOT NULL,
  min_value BIGINT NOT NULL,
  cache_value BIGINT NOT NULL,
  log_cnt BIGINT NOT NULL,
  is_cycled BOOL NOT NULL,
  is_called BOOL NOT NULL
);
ALTER TABLE queries ADD FOREIGN KEY ( database_connection_id ) REFERENCES database_connections ( id );
CREATE UNIQUE INDEX unique_key ON queries ( key );
ALTER TABLE query_versions ADD FOREIGN KEY ( query_id ) REFERENCES queries ( id );
ALTER TABLE query_versions ADD FOREIGN KEY ( query_id ) REFERENCES queries ( id );
