# RIOT API

## Introduccion

Este proyecto está hecho principalmente con Spring Boot v3.1.1, postgreSQL y JPA repository. 
El mismo proporciona endpoints que facilitan la conexion con la API de Riot Games para obtener
estadisticas de invocadores, partidas, campeones, etc. Mas adelante se detallará que endpoints consumen 
y almacenan los datos en la base de datos. 

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
Una vez logeados, podemos generar la apikey (que vence en 24 hs) y cambiarla por base de datos:

    -- si es por primera vez, insertamos datos
    INSERT INTO public.riot_api (id_riot_api, username, password, api_key, last_update)
    VALUES ('TU USERNAME', 'TU PSW', 'TU API KEY', 'FECHA GEN API KEY');

    -- update api key
    UPDATE public.riot_api
    SET api_key = 'NUEVA API KEY',
        last_update = 'FECHA UPDATE' WHERE id_riot_api = 1;

Hecho ésto, tenemos tres endpoints:

1. /riot-api/las/invocador
   1. [GET] /nombre/{name} : le damos el nombre del invocador y obtenemos un JSON con los datos del mismo. Ejemplo: http://localhost:8080/riot-api/las/invocador/nombre/PalitKing
   2. [GET] /idCuenta/{accountId} : si conocemos la accountId del invocador, lo podemos buscar. Retorna un JSON con los datos del mismo.
   3. [GET] /puuid/{puuid} : si conocemos la puuid del invocador, lo podemos buscar. Retorna un JSON con los datos del mismo.

## Base de Datos

Los datos de invocadores y campeones se guardan en las siguientes tablas, que se encuentran en la carpeta [databse](https://github.com/ginos1998/riotapi/tree/main/src/main/database/versions):

- summoner
- riot_api
- champ_data
  - champion
    - info
    - stats