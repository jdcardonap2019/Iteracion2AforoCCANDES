--Requerimiento Funcional 11
UPDATE ESPACIO
SET ESTADO= 'Deshabilitado',
    AFORO_ACTUAL= 0
WHERE ID_ESPACIO='?';
UPDATE VISITA
SET HORAFIN_OP = GETDATE()
WHERE IDESPACIO ='?' AND FECHAYHORA_OP< CONVERT(DATE,GETDATE()) AND TIPO_OP='Entrada';