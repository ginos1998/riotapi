# RIOT API

## Introduccion

Este proyecto consta de una API REST y está hecho principalmente con Spring Boot v3.1.1, postgreSQL y JPA repository. 
El mismo proporciona endpoints que facilitan la conexion con la API de Riot Games para obtener
estadisticas de invocadores, partidas, campeones, etc.

## Endpoints

Tenemos dos urls principales:
1. /riot-api/campeon
2. /riot-api/las/invocador

En 1, se consume la API pública de [éste link](https://ddragon.leagueoflegends.com/cdn/13.13.1/data/en_US/champion.json)
y tiene tres endpoints:

1. /riot-api/campeon
   1. [GET] /nombre : recibe el nombre del campeon que queremos buscar y retorna un JSON con sus datos.
    Ejemplo: http://localhost:8080/riot-api/campeon/nombre?champName=Aatrox 
   2. [GET] /todos : devuelve todos los campeones en formato JSON. Ejemplo: http://localhost:8080/riot-api/campeon/todos
   3. [POST] /importar/todos : importa todos los campeones y los almacena en la base de datos, con sus propiedades. Retorna 'OK' si todo salió bien.

En 2, se consume la API de Riot Games. Para ello, debemos tener una cuenta y logearnos en la [página para desarrolladores](https://developer.riotgames.com/). 
Una vez logeados, podemos generar la apikey (que vence cada 24 hs) y cambiarla en la base de datos:

    -- si es por primera vez, insertamos datos
    INSERT INTO public.riot_api (id_riot_api, username, password, api_key, last_update)
    VALUES ('TU USERNAME', 'TU PSW', 'TU API KEY', current_date);

    -- update api key
    UPDATE public.riot_api
    SET api_key = 'NUEVA API KEY',
        last_update = current_date 
    WHERE id_riot_api = 1;

Hecho ésto, tenemos tres endpoints:

1. /riot-api/las/invocador
   1. [GET] /nombre/{name} : le damos el nombre del invocador y obtenemos un JSON con los datos del mismo. Ejemplo: http://localhost:8080/riot-api/las/invocador/nombre/PalitKing
   2. [GET] /idCuenta/{accountId} : si conocemos la accountId del invocador, lo podemos buscar. Retorna un JSON con los datos del mismo.
   3. [GET] /puuid/{puuid} : si conocemos la puuid del invocador, lo podemos buscar. Retorna un JSON con los datos del mismo.

## Base de Datos

El proyecto tiene conexión a una instancia de base de datos en AWS. Sin embargo, la 
puerta de enlace a la misma no está publicada en éste repositorio.
