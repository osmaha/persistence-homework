create table if not exists students (
	id bigserial,
	email text not null unique,
	first_name text not null,
    last_name text not null,
	birthday date not null,
	scholarship boolean not null,
	status text not null,

	CONSTRAINT students_pkey PRIMARY KEY (id)
)
