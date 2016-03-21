# --- Created by Ebean DD
# TO stop Ebean DDL generation, remove this COMMENT AND START USING Evolutions

# --- !Ups

CREATE TABLE exercise (
  id        SERIAL NOT NULL,
  title     VARCHAR(255),
  content   VARCHAR(255),
  time      DATE,
  points    INT,
  userID_FK INT,
  CONSTRAINT pk_exercise PRIMARY KEY (id)
);

CREATE TABLE user (
  id          SERIAL NOT NULL,
  username    VARCHAR(255),
  email       VARCHAR(255),
  isModerator BOOLEAN,
  points      INT,
  CONSTRAINT pk_user PRIMARY KEY (id)
);


# --- !Downs

DROP TABLE IF EXISTS exercise CASCADE;
DROP TABLE IF EXISTS user CASCADE;

