# RIOT API

## Introduccion

Este proyecto está hecho principalmente con Spring Boot v3.1.1, postgreSQL y JPA repository. 
El mismo proporciona endpoints que facilitan la conexion con la API de Riot Games para obtener
estadisticas de invocadores, partidas, campeones, etc. Mas adelante se detallará que endpoints consumen 
y almacenan los datos en la base de datos. 

## Endpoints

Tenemos dos endpoints principales:
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
Una vez logeados, podemos generar la apikey (que vence en 24 hs) y cambiarla en la clase Constants.
Hecho ésto, tenemos tres endpoints:

1. /riot-api/las/invocador
   2. [GET] /nombre/{name} : le damos el nombre del invocador y obtenemos un JSON con los datos del mismo. Ejemplo: http://localhost:8080/riot-api/las/invocador/nombre/PalitKing
   3. [GET] /idCuenta/{accountId} : si conocemos la accountId del invocador, lo podemos buscar. Retorna un JSON con los datos del mismo.
   4. [GET] /puuid/{puuid} : si conocemos la puuid del invocador, lo podemos buscar. Retorna un JSON con los datos del mismo.

## Base de Datos

Los datos de invocadores y campeones se guardan en las siguientes tablas, que se encuentran en la carpeta database:

- summoner
- champ_data
  - champion
    - info
    - stats