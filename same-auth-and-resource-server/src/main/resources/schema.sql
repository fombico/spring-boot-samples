CREATE TABLE account (
  id INTEGER NOT NULL AUTO_INCREMENT,
  username VARCHAR(128) NOT NULL,
  password VARCHAR(256) NOT NULL,
  scopes VARCHAR(256) NOT NULL,
  PRIMARY KEY (id)
);