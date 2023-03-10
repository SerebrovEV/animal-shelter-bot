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
ADD COLUMN dog_user_id BIGINT REFERENCES dog_user(id);

--changeset mara:4
ALTER TABLE adopted_cat
    ADD COLUMN cat_user_id BIGINT REFERENCES cat_user(id);
 --changeset sev:3
create table dog_report
(
    id          bigserial primary key,
    date date,
    photo       text,
    text        text
);
create table cat_report
(
    id          bigserial primary key,
    date date,
    photo       text,
    text        text
);
--changeset sev:4
ALTER TABLE dog_report
    ADD COLUMN adopted_dog_id BIGINT REFERENCES adopted_dog(id);
ALTER TABLE cat_report
    ADD COLUMN adopted_cat_id BIGINT REFERENCES adopted_cat(id);