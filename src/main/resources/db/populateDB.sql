DELETE FROM date_time;
DELETE FROM currency;
DELETE FROM roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 1000;

INSERT INTO USERS (LOGIN, FULL_NAME, PASSWORD, ENABLED) VALUES
  ('admin', 'Administrator', 'adminp', TRUE),
  ('user','User', 'userp', TRUE);

INSERT INTO ROLES (USER_ID, ROLE) VALUES
  (1000, 'ROLE_ADMIN'),
  (1001, 'ROLE_USER');

INSERT INTO date_time (date_time) VALUES
  ('2018-05-31');

INSERT INTO CURRENCY (VCode, date_time, Vcurs) VALUES
  ('840', 1002, 62.6420),
  ('978', 1002, 72.5269);



