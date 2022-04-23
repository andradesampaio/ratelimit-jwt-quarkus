CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;
DROP TABLE IF EXISTS users_;
CREATE TABLE IF NOT EXISTS users_ (
  id INTEGER not NULL CONSTRAINT id_pk PRIMARY KEY,
  username VARCHAR(255) not NULL,
  password VARCHAR(255) not NULL,
  role VARCHAR(50) not NULL,
CONSTRAINT username_constraint UNIQUE (username));

INSERT INTO users_ (id, username, password, role)
VALUES (nextVal('hibernate_sequence'), 'draande', '$2a$10$Sp0C/bK7dk8bxuTtEaPZOeplArJwL/nEpR9XTb.yM9GCqMmw5pvHe', 'ADMIN');
INSERT INTO users_ (id, username, password, role)
VALUES (nextVal('hibernate_sequence'), 'asampaio', '$2a$10$Sp0C/bK7dk8bxuTtEaPZOeplArJwL/nEpR9XTb.yM9GCqMmw5pvHe', 'USER');
INSERT INTO users_ (id, username, password, role)
VALUES (nextVal('hibernate_sequence'), 'guest', '$2a$10$Sp0C/bK7dk8bxuTtEaPZOeplArJwL/nEpR9XTb.yM9GCqMmw5pvHe', 'GUEST');
