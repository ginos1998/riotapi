BEGIN;
-- auto-generated definition
create table champ_data
(
    type          varchar(50),
    format        varchar(50),
    version       varchar(10),
    last_update   date,
    id_champ_data integer default nextval('champ_data_id_seq'::regclass) not null
        constraint pk_id
            primary key
);
-- auto-generated definition
create table champion
(
    key           bigint       not null
        constraint pk_champ_key
            primary key,
    id            varchar(150) not null,
    id_champ_data bigint,
    name          varchar(200) not null,
    title         varchar(200) not null
);

-- auto-generated definition
create table info
(
    id_info    serial
        constraint info_pk_id_info
            primary key,
    key        bigint  not null,
    attack     integer not null,
    defense    integer not null,
    difficulty integer not null,
    magic      integer not null
);

comment on column info.key is 'primary key of champion';

-- auto-generated definition
create table stats
(
    id_stats                integer default nextval('stats_id_seq'::regclass) not null
        constraint stats_id_pk
            primary key,
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
    key                     bigint                                            not null
);

comment on column stats.key is 'primary key of champion';

-- auto-generated definition
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

create index index_name
    on summoner (id);

COMMIT;