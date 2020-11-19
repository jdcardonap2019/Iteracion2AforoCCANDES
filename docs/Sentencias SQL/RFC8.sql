--RFC8
SELECT aidi, name, horaInicio, horaFin, idU, visitante.nombre
FROM(SELECT aidi, name, visita.fechayhora_op as horaInicio, visita.horafin_op as horaFin, VISITA.IDCARNET as idU, COUNT() as numVisitas
FROM(SELECT IDESPACIO as aidi, NOMBRE as name
FROM LOCAL_COMERCIAL
WHERE NOMBRE='Hayes LLC')
INNER JOIN VISITA ON VISITA.IDESPACIO=aidi
GROUP BY VISITA.IDCARNET)
INNER JOIN CARNET ON CARNET.ID_CARNET=idU
INNER JOIN VISITANTE ON carnet.cedula=VISITANTE.CEDULA;