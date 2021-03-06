(
--i
SELECT VISITANTE.CEDULA as cedula, VISITANTE.NOMBRE as nombre, VISITANTE.TELEFONO as telefono, VISITANTE.NOMBRE_CONTACTO as contacto, VISITANTE.TELEFONO_CONTACTO as contactoTelefono, VISITANTE.CORREO as correo
FROM(SELECT carne, COUNT(DISTINCT mes)
FROM(SELECT EXTRACT(MONTH FROM CAST(FECHAYHORA_OP as DATE)) as mes,IDCARNET as carne
FROM VISITA
GROUP BY EXTRACT(MONTH FROM CAST(FECHAYHORA_OP as DATE)),IDCARNET)
GROUP BY carne
HAVING COUNT (DISTINCT mes)='12')
INNER JOIN CARNET ON carnet.id_carnet=carne
INNER JOIN VISITANTE ON VISITANTE.CEDULA=carnet.cedula) UNION
(
--ii
SELECT VISITANTE.CEDULA as cedula, VISITANTE.NOMBRE as nombre, VISITANTE.TELEFONO as telefono, VISITANTE.NOMBRE_CONTACTO as contacto, VISITANTE.TELEFONO_CONTACTO as contactoTelefono, VISITANTE.CORREO as correo
FROM (SELECT dia, carne, COUNT (DISTINCT Espacio) as espaciosVisitados
FROM(SELECT EXTRACT(DAY FROM CAST(FECHAYHORA_OP as DATE)) as dia, IDCARNET as carne, IDESPACIO as Espacio, TIPO_OP 
FROM VISITA
GROUP BY FECHAYHORA_OP, IDCARNET, IDESPACIO, TIPO_OP, EXTRACT(DAY FROM CAST(FECHAYHORA_OP as DATE))
HAVING EXTRACT(MONTH FROM CAST(FECHAYHORA_OP as DATE))='12' AND EXTRACT(YEAR FROM CAST(FECHAYHORA_OP as DATE))='2020' AND TIPO_OP='Entrada')
GROUP BY dia, carne
HAVING COUNT (DISTINCT Espacio)>=4)
INNER JOIN CARNET ON carnet.id_carnet=carne
INNER JOIN VISITANTE ON VISITANTE.CEDULA=carnet.cedula) UNION
(
--iii
SELECT VISITANTE.CEDULA as cedula, VISITANTE.NOMBRE as nombre, VISITANTE.TELEFONO as telefono, VISITANTE.NOMBRE_CONTACTO as contacto, VISITANTE.TELEFONO_CONTACTO as contactoTelefono, VISITANTE.CORREO as correo
FROM(SELECT carne, COUNT (DISTINCT tipo) tiposLocalesVisitados
FROM(SELECT dia, carne, Espacio, LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO as tipo
FROM(SELECT EXTRACT(DAY FROM CAST(FECHAYHORA_OP as DATE)) as dia, IDCARNET as carne, IDESPACIO as Espacio, TIPO_OP
FROM VISITA
GROUP BY FECHAYHORA_OP, IDCARNET, IDESPACIO, TIPO_OP, EXTRACT(DAY FROM CAST(FECHAYHORA_OP as DATE))
HAVING EXTRACT(MONTH FROM CAST(FECHAYHORA_OP as DATE))='12' AND EXTRACT(YEAR FROM CAST(FECHAYHORA_OP as DATE))='2020' AND TIPO_OP='Entrada')
INNER JOIN LOCAL_COMERCIAL ON local_comercial.idespacio=Espacio)
GROUP BY carne
HAVING COUNT (DISTINCT tipo)>=2)
INNER JOIN CARNET ON carnet.id_carnet=carne
INNER JOIN VISITANTE ON VISITANTE.CEDULA=carnet.cedula);