--REQUERIMIENTO DE CONSULTA 3
--PARA EL INDICE DE AFORO DEL CC DEL ADMIN DEL ESPACIO
SELECT AforoEnCC/AforoMaximo as IndiceAforoEnCC, IdEspacio, AforoEnEstablecimiento/AforoMaximoXd as AforoEnEspacio
FROM(SELECT SUM(aforo_actual) as AforoEnCC, SUM(aforo_total) as AforoMaximo
    FROM ESPACIO),
    (SELECT ID_ESPACIO as IdEspacio, aforo_actual as AforoEnEstablecimiento, aforo_total as AforoMaximoXd
    FROM ESPACIO
    WHERE ID_ESPACIO=?);
--PARA EL INDICE DE AFORO DEL CC PARA ADIMINISTRADOR DEL CENTRO
--Segun el establecimiento
SELECT AforoEnCC/AforoMaximo as IndiceAforoEnCC, IdEspacio, AforoEnEstablecimiento/AforoMaximoXd as AforoEnEspacio
FROM(SELECT SUM(aforo_actual) as AforoEnCC, SUM(aforo_total) as AforoMaximo
    FROM ESPACIO),
    (SELECT ID_ESPACIO as IdEspacio, aforo_actual as AforoEnEstablecimiento, aforo_total as AforoMaximoXd
    FROM ESPACIO
    WHERE ID_ESPACIO=?);
--Segun el tipo de establecimiento
SELECT AforoEnCC/AforoMaximo as IndiceAforoEnCC,TipoEspacio,  AforoActualEspacio/AforoTotalEspacio as IndiceTipoEstablecimiento
FROM(SELECT SUM(aforo_actual) as AforoEnCC, SUM(aforo_total) as AforoMaximo
    FROM ESPACIO),
    (SELECT TipoEspacio, SUM(espacio.aforo_actual) as AforoActualEspacio, SUM(espacio.aforo_total) as AforoTotalEspacio
    FROM(SELECT IDESPACIO as IdEspacio, TIPO_ESTABLECIMIENTO as TipoEspacio
        FROM LOCAL_COMERCIAL
        WHERE TIPO_ESTABLECIMIENTO=?)
        INNER JOIN ESPACIO ON espacio.id_espacio=IdEspacio group by TipoEspacio);