INSERT INTO users (username, password, enabled) VALUES ('echocano','$2a$10$O9wxmH/AeyZZzIS09Wp8YOEMvFnbRVJ8B4dmAMVSGloR62lj.yqXG',true);
INSERT INTO users (username, password, enabled) VALUES ('admin','$2a$10$DOMDxjYyfZ/e7RcBfUpzqeaCs8pLgcizuiQWXPkU35nOhZlFcE9MS',true);

INSERT INTO authorities(user_id, authority) VALUES(1, 'ROLE_USER');
INSERT INTO authorities(user_id, authority) VALUES(2, 'ROLE_USER');
INSERT INTO authorities(user_id, authority) VALUES(3, 'ROLE_ADMIN');