CREATE TABLE database_connections
(
  id INT PRIMARY KEY NOT NULL,
  name VARCHAR NOT NULL,
  url VARCHAR NOT NULL,
  username VARCHAR NOT NULL,
  password VARCHAR NOT NULL
);
CREATE TABLE queries
(
  id INT PRIMARY KEY NOT NULL,
  key VARCHAR NOT NULL,
  database_connection_id INT NOT NULL
);
CREATE TABLE query_versions
(
  id INT PRIMARY KEY NOT NULL,
  content VARCHAR NOT NULL,
  version INT NOT NULL,
  is_current_version BOOL NOT NULL,
  query_id INT NOT NULL
);
ALTER TABLE queries ADD FOREIGN KEY ( database_connection_id ) REFERENCES database_connections ( id );
CREATE UNIQUE INDEX unique_key ON queries ( key );
ALTER TABLE query_versions ADD FOREIGN KEY ( query_id ) REFERENCES queries ( id );
