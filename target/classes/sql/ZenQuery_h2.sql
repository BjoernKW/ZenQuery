CREATE TABLE database_connections (
id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
name varchar(200) NOT NULL,
url varchar(2048) NOT NULL,
username varchar(200) NOT NULL,
password varchar(200) NOT NULL
);

CREATE TABLE queries (
id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
key varchar(256) NOT NULL,
database_connection_id bigint NOT NULL
);
ALTER TABLE queries ADD FOREIGN KEY (database_connection_id) REFERENCES public.database_connections(id);

CREATE TABLE query_versions (
id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
content clob NOT NULL,
version bigint NOT NULL,
is_current_version boolean NOT NULL,
query_id bigint NOT NULL
);
ALTER TABLE query_versions ADD FOREIGN KEY (query_id) REFERENCES public.queries(id);
