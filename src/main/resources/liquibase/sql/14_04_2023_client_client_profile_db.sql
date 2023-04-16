create schema if not exists security;

create table if not exists security.client
(
    id         bigserial primary key,
    uuid       uuid        not null unique,
    first_name varchar(30) not null,
    last_name  varchar(30) not null,
    middle_name varchar(30),
    registered timestamp   not null
);

create table if not exists security.client_profile
(
    id        bigserial primary key,
    email     varchar(50) not null unique,
    password  varchar     not null,
    client_id bigserial references security.client (id)
);