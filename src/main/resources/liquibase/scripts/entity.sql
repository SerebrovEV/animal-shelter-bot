-- liquibase formatted sql

--changeset sev:1
create table bot_user(
    id            bigserial,
    chat_id       bigint,
    user_name text,
    phone_number  bigint
);
--changeset mara:1
create table adopted_dog(
   id             BIGSERIAL,
   dog_name       TEXT,
   adoption_date  DATE,
   trial_period   INTEGER
);
--changeset mara:2
 create table adopted_cat(
     id             BIGSERIAL,
     cat_name       TEXT,
     adoption_date  DATE,
     trial_period   INTEGER
 );