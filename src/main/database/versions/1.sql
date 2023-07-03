-- create database
BEGIN;
CREATE DATABASE riot
    WITH
    OWNER = ginos
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- summoner datatable
create table summoner
(
    id              varchar not null
        constraint id_pk
            primary key,
    account_id      varchar not null,
    puuid           varchar not null,
    name            varchar not null,
    profile_icon_id integer,
    revision_date   bigint,
    summoner_level  integer
);

comment on table summoner is 'riot summoner';

alter table summoner
    owner to ginos;

create index index_name
    on summoner (id);

--
COMMIT;

