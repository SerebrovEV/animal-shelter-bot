-- liquibase formatted sql

--changeset sev:1
create table bot_user(

    id            bigserial,
    chat_id       bigint,
    user_name text,
    phone_number  bigint
);