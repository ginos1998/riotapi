BEGIN;
-- at first time
INSERT INTO public.riot_api (username, password, api_key, last_update)
VALUES ('user', 'password', 'api-key', current_date);

-- update api key
UPDATE public.riot_api
SET api_key = 'new api-key',
    last_update = current_date
WHERE id_riot_api = 1;

COMMIT;
