-- create database
BEGIN;
CREATE DATABASE riotapi
    WITH
    OWNER = ginos
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;


COMMIT;

