# RIOT API

## Introduccion

Este proyecto consta de una API REST y está hecho principalmente con Spring Boot v3.1.1, postgreSQL y JPA repository. 
El mismo proporciona endpoints que facilitan la conexion con la API de Riot Games para obtener
estadisticas de invocadores, partidas, campeones, etc.

## Endpoints

La API cuenta con endpoints para obtener informacion de campeones, invocadores y partidas. En lo posible, primero se busca en la base de datos, y si no encuentra los datos solicitados, hace una peticion a la api de riot. Los datos se guardan opcionalmente, al llamar un GET que tenga la opcion.

1. Campeones
   - [REQ] /riot-api/campeon
      - [GET] /nombre
      - [GET] /todos
      - [POST] /importar/todos
3. Invocadores
   - [REQ] /riot-api/las/invocador
      - [GET] ?name=&accountId=&puuid&saveIfExists=
5. Partidas
   - [REQ] /riot-api/las/partidas
       - [GET] /by-puuid/{puuid}?{params}
       - [GET] /by-summoner-name/{sumName}?{params}
       - [GET] /by-matchId/{matchId}?saveData=
       - [GET] /current-match/{sumName}
   - *params(opcionales): startTime=&endTime=&queue=&type=&start=&count=&save

En 1, se consume la API pública de [éste link](https://ddragon.leagueoflegends.com/cdn/13.13.1/data/en_US/champion.json)

En 2 y 3, se consume la API de Riot Games. Para ello, debemos tener una cuenta y logearnos en la [página para desarrolladores](https://developer.riotgames.com/). 
Una vez logeados, podemos generar la apikey (que vence cada 24 hs) y cambiarla en la base de datos:

    -- si es por primera vez, insertamos datos
    INSERT INTO public.riot_api (id_riot_api, username, password, api_key, last_update)
    VALUES ('TU USERNAME', 'TU PSW', 'TU API KEY', current_date);

    -- update api key
    UPDATE public.riot_api
    SET api_key = 'NUEVA API KEY',
        last_update = current_date 
    WHERE id_riot_api = 1;

## Base de Datos

El proyecto tiene conexión a una instancia de base de datos en AWS. Sin embargo, la 
puerta de enlace a la misma no está publicada en este repositorio. 
Para agregar las variables de entorno en el localhost,

DB_HOST= ;
DB_NAME= ;
DB_USER= ;
DB_PSW= 

De todas formas, más allá de no tener estos datos, uno puede obtener la información solicitada en cada endpoint, 
ya que si lo buscado no está en la bd, se hace una petición a la respectiva api.
