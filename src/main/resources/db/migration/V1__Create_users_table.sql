CREATE TABLE users (
	id serial4 NOT NULL,
	email varchar NOT NULL,
	password varchar NOT NULL,
	role varchar NOT NULL,
	created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at timestamp NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_unique UNIQUE (email)
);