BEGIN;
-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create sequence public.champ_data_id_seq as integer;
alter sequence public.champ_data_id_seq owner to ginos;
create table public.champ_data
(
    type          varchar(50),
    format        varchar(50),
    version       varchar(10),
    last_update   date,
    id_champ_data integer default nextval('champ_data_id_seq'::regclass) not null
        constraint pk_id
            primary key
);
alter sequence public.champ_data_id_seq owned by public.champ_data.id_champ_data;
alter table public.champ_data
    owner to ginos;

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create table public.info
(
    key        bigint  not null
        constraint info_key_pk
            primary key,
    attack     integer not null,
    defense    integer not null,
    difficulty integer not null,
    magic      integer not null
);
comment on column public.info.key is 'primary key of champion';
alter table public.info
    owner to ginos;

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create sequence public.riot_api_id_riot_api_seq
    as integer;
alter sequence public.riot_api_id_riot_api_seq owner to ginos;
create table public.riot_api
(
    id_riot_api serial
        constraint riot_api_pk_id
            primary key,
    username    text not null,
    password    text not null,
    api_key     text not null,
    last_update date not null
);
alter sequence public.riot_api_id_riot_api_seq owned by public.riot_api.id_riot_api;
alter table public.riot_api
    owner to ginos;

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create table public.spell
(
    spell_id integer not null
        constraint spell_pk
            primary key,
    spell    varchar not null
);
alter table public.spell
    owner to ginos;

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create table public.champion
(
    key           bigint       not null
        constraint pk_champ_key
            primary key,
    champion_id   varchar(150) not null
        constraint champion_pk
            unique,
    id_champ_data bigint,
    name          varchar(200) not null,
    title         varchar(200) not null
);
alter table public.champion
    owner to ginos;

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create table public.stats
(
    hp                      integer,
    hp_per_level            integer,
    mp                      integer,
    mp_per_level            integer,
    move_speed              integer,
    armor                   integer,
    armor_per_level         double precision,
    spell_block             integer,
    spell_block_per_level   double precision,
    attack_range            integer,
    hp_regen                integer,
    hp_regen_per_level      integer,
    mp_regen                integer,
    mp_regen_per_level      integer,
    crit                    integer,
    crit_per_level          integer,
    attack_damage           integer,
    attack_damage_per_level integer,
    attack_speed_per_level  double precision,
    attack_speed            double precision,
    key                     bigint not null
        constraint stats_key_pk
            primary key
        constraint stats_key_fk
            references public.champion
);
comment on column public.stats.key is 'primary key of champion';
alter table public.stats
    owner to ginos;

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create table public.summoner
(
    summoner_id     varchar not null
        constraint id_pk
            primary key,
    account_id      varchar,
    puuid           varchar not null,
    name            varchar not null,
    profile_icon_id integer,
    revision_date   bigint,
    summoner_level  integer
);
comment on table public.summoner is 'riot summoner';
alter table public.summoner
    owner to ginos;
create index index_name
    on public.summoner (summoner_id);
create unique index summoner_account_id_uindex
    on public.summoner (account_id);

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create table public.match
(
    match_id   bigint           not null
        constraint match_pk
            primary key,
    mode       varchar,
    name       varchar,
    type       varchar,
    version    varchar,
    map_id     integer,
    creation   bigint default 0 not null,
    duration   bigint default 0 not null,
    start_time bigint default 0 not null,
    end_time   bigint default 0 not null
);
alter table public.match
    owner to ginos;

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--
create sequence public.summoner_match_summoner_match_id_seq
    as integer;
alter sequence public.summoner_match_summoner_match_id_seq owner to ginos;
create table public.summoner_match
(
    summoner_match_id serial
        constraint summoner_match_pk
            primary key,
    match_id          bigint  not null
        constraint sum_match_fk_match
            references public.match,
    summoner_id       varchar not null
        constraint sum_match_fk_sum
            references public.summoner,
    lane              varchar,
    win               boolean,
    champion_key      bigint,
    team_id           integer
);

alter table public.summoner_match
    owner to ginos;
alter sequence public.summoner_match_summoner_match_id_seq owned by public.summoner_match.summoner_match_id;

COMMIT;