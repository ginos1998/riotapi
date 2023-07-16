-- create database
BEGIN;
CREATE DATABASE riot
    WITH
    OWNER = ginos
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- summonerDTO datatable
create table summonerDTO
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

comment on table summonerDTO is 'riot summonerDTO';

alter table summonerDTO
    owner to ginos;

create index index_name
    on summonerDTO (id);

--
COMMIT;

