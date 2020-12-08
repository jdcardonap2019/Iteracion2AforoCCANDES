ALTER TABLE VISITANTE
DROP CONSTRAINT fk_espacio_visitante;

ALTER TABLE CARNET
DROP CONSTRAINT fk_cedula;

ALTER TABLE VISITA
DROP CONSTRAINT fk_visita_lector;

ALTER TABLE VISITA
DROP CONSTRAINT fk_visita_carnet;

ALTER TABLE LECTOR
DROP CONSTRAINT fk_espacio;



ALTER TABLE LOCAL_COMERCIAL
DROP CONSTRAINT fk_local;

ALTER TABLE BAÑO
DROP CONSTRAINT fk_baño;

ALTER TABLE PARQUEADERO
DROP CONSTRAINT fk_parqueadero;


ALTER TABLE VISITA
DROP CONSTRAINT fk_visita_espacio;

ALTER TABLE ESPACIO
DROP CONSTRAINT ck_estado;

ALTER TABLE VISITANTE
DROP CONSTRAINT ck_estado_visitante;

DROP INDEX IDX_VISITANTEHORA;
DELETE FROM BAÑO;
DELETE FROM CENTRO_COMERCIAL;
DELETE FROM ESPACIO;
DELETE FROM LECTOR;
DELETE FROM PARQUEADERO;
DELETE FROM VISITA;
DELETE FROM VISITANTE;
DELETE FROM LOCAL_COMERCIAL;

DROP TABLE BAÑO;
DROP TABLE CARNET;
DROP TABLE CENTRO_COMERCIAL;
DROP TABLE LECTOR;
DROP TABLE PARQUEADERO;
DROP TABLE VISITA;
DROP TABLE VISITANTE;
DROP TABLE LOCAL_COMERCIAL;
DROP TABLE ESPACIO;
DROP SEQUENCE centro_comercial_sequence;