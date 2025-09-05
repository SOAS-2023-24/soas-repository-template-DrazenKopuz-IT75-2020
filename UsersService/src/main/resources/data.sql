insert into users(id, email, password, role)
values  (nextval('USERS_SEQ'), 'owner@uns.ac.rs', 'password', 'OWNER'),
		(nextval('USERS_SEQ'), 'admin@uns.ac.rs', 'password', 'ADMIN'),
		(nextval('USERS_SEQ'), 'user@uns.ac.rs', 'password', 'USER');