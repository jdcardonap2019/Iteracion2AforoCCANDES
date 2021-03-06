SELECT VISITANTE.CEDULA, VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.NOMBRE_CONTACTO, VISITANTE.TELEFONO_CONTACTO, VISITANTE.CORREO 
FROM(SELECT DISTINCT(IDCARNET)
FROM VISITA
WHERE FECHAYHORA_OP BETWEEN to_date('2020-12-14:07:00:00', 'YYYY-MM-DD:HH24:MI:SS') AND to_date('2020-12-18:23:00:00', 'YYYY-MM-DD:HH24:MI:SS')
AND TIPO_OP='Entrada'
AND IDESPACIO=11)
RIGHT JOIN CARNET ON CARNET.ID_CARNET=IDCARNET
INNER JOIN VISITANTE ON VISITANTE.CEDULA=CARNET.CEDULA
WHERE IDCARNET IS NULL;