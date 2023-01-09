-- liquibase formatted sql

--changeset sev:1
create table bot_user(

    id            bigserial,
    chat_id       bigint,
    user_name text,
    phone_number  bigint
);
--changeset mara:1
 create table adopted_pet(

     id             BIGSERIAL,
     user_id        BIGINT,
     pet_name       TEXT,
     adoption_date  DATE,
     trial_period   INTEGER
 );