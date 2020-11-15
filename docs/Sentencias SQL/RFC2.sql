--REQUERIMIENTO DE CONSULTA 2
SELECT LOCAL_COMERCIAL.idespacio as IdEstablecimiento,local_comercial.nombre, local_comercial.tipo_establecimiento as Tipo,contadorVisitas
FROM(SELECT IDESPACIO as IDESPACIOXD, COUNT(DISTINCT IDCARNET)as contadorVisitas
    FROM VISITA
    WHERE FECHAYHORA_OP BETWEEN ? AND ?
    GROUP BY IDESPACIO
    HAVING COUNT(DISTINCT IDCARNET)>0)
    INNER JOIN LOCAL_COMERCIAL ON IDESPACIOXD=LOCAL_COMERCIAL.idespacio
    WHERE ROWNUM<=20
    ORDER BY contadorVisitas DESC;