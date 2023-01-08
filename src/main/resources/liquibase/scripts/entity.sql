-- liquibase formatted sql

--changeset sev:1
create table bot_user(

    id            bigserial,
    chat_id       bigint,
    user_name text,
    phone_number  bigint
);

--changeset sev:2
create table report
(
    id          bigserial primary key,
    photo       oid,
    text        text
);