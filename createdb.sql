
DROP ROLE IF EXISTS exsolnet;
CREATE ROLE exsolnet LOGIN PASSWORD 'exsolnet';

create database exsolnetdb OWNER exsolnet;
