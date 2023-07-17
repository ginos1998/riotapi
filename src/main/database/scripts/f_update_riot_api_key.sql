BEGIN;
-- si es por primera vez
INSERT INTO public.riot_api (id_riot_api, username, password, api_key, last_update)
VALUES ('TU USERNAME', 'TU PSW', 'TU API KEY', 'FECHA GEN API KEY');

-- update api key
UPDATE public.riot_api
SET api_key = 'NUEVA API KEY',
    last_update = 'FECHA UPDATE' WHERE id_riot_api = 1;

COMMIT;
