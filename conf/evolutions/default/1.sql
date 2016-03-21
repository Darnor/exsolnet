# --- Created by Ebean DD
# TO stop Ebean DDL generation, remove this COMMENT AND START USING Evolutions

# --- !Ups

CREATE TABLE exercise (
  id        SERIAL NOT NULL,
  title     VARCHAR(255),
  content   VARCHAR(255),
  time      DATE,
  points    INTEGER,
  userID_FK INTEGER,
  CONSTRAINT pk_exercise PRIMARY KEY (id)
);

CREATE TABLE account (
  id          SERIAL NOT NULL,
  username    VARCHAR(255),
  email       VARCHAR(255),
  isModerator BOOLEAN,
  points      INTEGER,
  userid_FK   BIGINT REFERENCES exercise(id),
  CONSTRAINT pk_account PRIMARY KEY (id)
);


# --- !Downs

DROP TABLE IF EXISTS exercise CASCADE;
DROP TABLE IF EXISTS account CASCADE;

