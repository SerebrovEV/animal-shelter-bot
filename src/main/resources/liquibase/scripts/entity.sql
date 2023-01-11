-- liquibase formatted sql

--changeset sev:1
create table dog_user(

    id            bigserial,
    chat_id       bigint,
    user_name text,
    phone_number  bigint
);
--changeset sev:2
create table cat_user(

    id            bigserial,
    chat_id       bigint,
    user_name text,
    phone_number  bigint
);