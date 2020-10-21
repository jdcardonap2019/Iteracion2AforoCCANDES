--REQUERIMIENTO DE CONSULTA 3
--PARA EL INDICE DE AFORO DEL CC
SELECT *
FROM(SELECT SUM(aforo_actual) as AforoEnCC
    FROM ESPACIO),
    (SELECT aforo_actual as AforoEnEstablecimiento
    FROM ESPACIO
    WHERE ID_ESPACIO=?);
--PARA EL INDICE DE AFORO DEL CC PARA ADIMINISTRADOR DEL CENTRO
--Segun el establecimiento
SELECT *
FROM(SELECT SUM(aforo_actual) as AforoEnCC
    FROM ESPACIO),
    (SELECT aforo_actual as AforoEnEstablecimiento
    FROM ESPACIO
    WHERE ID_ESPACIO=?);
--Segun el tipo de establecimiento
SELECT AforoEnCC, TipoEspacio, AforoEnEspacio
FROM(SELECT SUM(aforo_actual) as AforoEnCC
    FROM ESPACIO),
    (SELECT espacio.aforo_actual as AforoEnEspacio, TipoEspacio
    FROM(SELECT IDESPACIO as IdEspacio, TIPO_ESTABLECIMIENTO as TipoEspacio
        FROM LOCAL_COMERCIAL
        WHERE TIPO_ESTABLECIMIENTO=?)
        INNER JOIN ESPACIO ON espacio.id_espacio=IdEspacio);
-----