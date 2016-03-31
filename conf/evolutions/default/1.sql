# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table exercise (
  id                        bigserial not null,
  title                     varchar(255),
  content                   varchar(255),
  contenttemp               varchar(255),
  time                      date,
  points                    integer,
  constraint pk_exercise primary key (id))
;


# --- !Downs

drop table if exists exercise cascade;

