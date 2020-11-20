--RFC7
--Fecha Mayor afluencia
(SELECT A.flecho as flechin,B.SumaTotalAforoMaximo
FROM(SELECT flecho, jjjj, establi, idEsXd, SUM(numVisitas) as Sumavisitas
FROM(SELECT idEsXd, flecho, numVisitas, LOCAL_COMERCIAL.NOMBRE as jjjj, LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO as establi
FROM(SELECT IDESPACIO as idEsXd, FECHAYHORA_OP as flecho, COUNT(TIPO_OP) as NumVisitas
FROM VISITA
WHERE FECHAYHORA_OP BETWEEN to_date('2020-12-14:14:00:00', 'YYYY-MM-DD:HH24:MI:SS') AND to_date('2020-12-14:19:00:00', 'YYYY-MM-DD:HH24:MI:SS')
GROUP BY IDESPACIO, FECHAYHORA_OP)
INNER JOIN LOCAL_COMERCIAL ON LOCAL_COMERCIAL.IDESPACIO=idEsXd AND LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO='Restaurante')
GROUP BY flecho,  jjjj, establi, idEsXd
ORDER BY SUM(numVisitas) DESC)A,
(SELECT SUM(cd) as SumaTotalAforoMaximo
FROM(SELECT ID_ESPACIO as idd, AFORO_TOTAL as cd
FROM ESPACIO)
INNER JOIN LOCAL_COMERCIAL ON LOCAL_COMERCIAL.IDESPACIO=idd AND local_comercial.tipo_establecimiento='Restaurante') B
WHERE ROWNUM<2)UNION
(SELECT A.flecho2 as flechin2, b.noventaporcientoaforomaximo as XD2
FROM(SELECT flecho2, jjjj2, establi2, idEsXd2, SUM(numVisitas2) as Sumavisitas2
FROM(SELECT idEsXd2, flecho2, numVisitas2, LOCAL_COMERCIAL.NOMBRE as jjjj2, LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO as establi2
FROM(SELECT IDESPACIO as idEsXd2, FECHAYHORA_OP as flecho2, COUNT(TIPO_OP) as NumVisitas2
FROM VISITA
WHERE FECHAYHORA_OP BETWEEN to_date('2020-12-14:14:00:00', 'YYYY-MM-DD:HH24:MI:SS') AND to_date('2020-12-14:19:00:00', 'YYYY-MM-DD:HH24:MI:SS')
GROUP BY IDESPACIO, FECHAYHORA_OP)
INNER JOIN LOCAL_COMERCIAL ON LOCAL_COMERCIAL.IDESPACIO=idEsXd2 AND LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO='Restaurante')
GROUP BY flecho2,  jjjj2, establi2, idEsXd2
ORDER BY SUM(numVisitas2) DESC)A,
(SELECT SUM(cd)*0.9 as NoventaPorcientoAforoMaximo
FROM(SELECT ID_ESPACIO as idd, AFORO_TOTAL as cd
FROM ESPACIO)
INNER JOIN LOCAL_COMERCIAL ON LOCAL_COMERCIAL.IDESPACIO=idd AND local_comercial.tipo_establecimiento='Restaurante') B
WHERE A.SumaVisitas2>b.noventaporcientoaforomaximo AND ROWNUM<2)UNION( 
SELECT A.flecho3 as flechin3, b.diezporcientoaforomaximo as XD3
FROM(SELECT flecho3, jjjj3, establi3, idEsXd3, SUM(numVisitas3) as Sumavisitas3
FROM(SELECT idEsXd3, flecho3, numVisitas3, LOCAL_COMERCIAL.NOMBRE as jjjj3, LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO as establi3
FROM(SELECT IDESPACIO as idEsXd3, FECHAYHORA_OP as flecho3, COUNT(TIPO_OP) as NumVisitas3
FROM VISITA
WHERE FECHAYHORA_OP BETWEEN to_date('2020-12-14:14:00:00', 'YYYY-MM-DD:HH24:MI:SS') AND to_date('2020-12-14:19:00:00', 'YYYY-MM-DD:HH24:MI:SS')
GROUP BY IDESPACIO, FECHAYHORA_OP)
INNER JOIN LOCAL_COMERCIAL ON LOCAL_COMERCIAL.IDESPACIO=idEsXd3 AND LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO='Restaurante')
GROUP BY flecho3,  jjjj3, establi3, idEsXd3
ORDER BY SUM(numVisitas3) ASC)A,
(SELECT SUM(cd)*0.1 as DiezPorcientoAforoMaximo
FROM(SELECT ID_ESPACIO as idd, AFORO_TOTAL as cd
FROM ESPACIO)
INNER JOIN LOCAL_COMERCIAL ON LOCAL_COMERCIAL.IDESPACIO=idd AND local_comercial.tipo_establecimiento='Restaurante') B
WHERE A.SumaVisitas3<b.diezporcientoaforomaximo AND ROWNUM<2);