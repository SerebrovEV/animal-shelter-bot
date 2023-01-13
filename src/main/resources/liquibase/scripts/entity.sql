-- liquibase formatted sql

--changeset sev:1
create table dog_user(

    id            bigserial PRIMARY KEY,
    chat_id       bigint,
    user_name text,
    phone_number  bigint
);
--changeset sev:2
create table cat_user(

    id            bigserial PRIMARY KEY ,
    chat_id       bigint,
    user_name text,
    phone_number  bigint
);
--changeset mara:1
create table adopted_dog(
   id             BIGSERIAL PRIMARY KEY ,
   dog_name       TEXT,
   adoption_date  DATE,
   trial_period   INTEGER DEFAULT '30'
);
--changeset mara:2
 create table adopted_cat(
     id             BIGSERIAL PRIMARY KEY ,
     cat_name       TEXT,
     adoption_date  DATE,
     trial_period   INTEGER DEFAULT '30'
 );
--changeset mara:3
ALTER TABLE adopted_dog
ADD COLUMN dog_user_id BIGINT REFERENCES adopted_dog(id);

--changeset mara:4
ALTER TABLE adopted_cat
    ADD COLUMN cat_user_id BIGINT REFERENCES adopted_cat(id);