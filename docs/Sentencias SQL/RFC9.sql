--RFC9 
SELECT fecha, usuarioCarnet, VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.CORREO, op, espacio
FROM(SELECT visitaxs.IDESPACIO espacio, visitaxs.FECHAYHORA_OP fecha, visitaxs.IDCARNET as usuarioCarnet, visitaxs.tipo_op as op
FROM VISITA visitaxs, (SELECT * FROM VISITA) visita2
WHERE visitaxs.IDESPACIO=visita2.IDESPACIO
AND visitaxs.idlector=visita2.IDLECTOR
AND visitaxs.fechayhora_op=visita2.fechayhora_op
AND NOT visitaxs.idcarnet=visita2.idcarnet
AND visitaxs.fechayhora_op BETWEEN ?-10 AND ?)
INNER JOIN CARNET ON CARNET.ID_CARNET=usuarioCarnet
INNER JOIN VISITANTE ON CARNET.CEDULA=VISITANTE.CEDULA
GROUP BY fecha,usuarioCarnet,VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.CORREO, op, espacio
ORDER BY espacio ASC;
