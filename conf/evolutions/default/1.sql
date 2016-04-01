# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        bigserial not null,
  content                   varchar(255),
  user_id                   bigint,
  solution_id               bigint,
  exercise_id               bigint,
  constraint pk_comment primary key (id))
;

create table exercise (
  id                        bigserial not null,
  content                   varchar(255),
  time                      date,
  points                    integer,
  title                     varchar(255),
  user_id                   bigint,
  constraint pk_exercise primary key (id))
;

create table report (
  id                        bigserial not null,
  message                   varchar(255),
  user_id                   bigint,
  solution_id               bigint,
  exercise_id               bigint,
  comment_id                bigint,
  constraint pk_report primary key (id))
;

create table solution (
  id                        bigserial not null,
  content                   varchar(255),
  time                      date,
  points                    integer,
  official                  boolean,
  exercise_id               bigint,
  user_id                   bigint,
  constraint pk_solution primary key (id))
;

create table tag (
  id                        bigserial not null,
  name                      varchar(255),
  is_main_tag               boolean,
  constraint pk_tag primary key (id))
;

create table track (
  id                        bigserial not null,
  tag_id                    bigint,
  user_id                   bigint,
  constraint pk_track primary key (id))
;

create table exoluser (
  id                        bigserial not null,
  email                     varchar(255),
  password                  varchar(255),
  points                    integer,
  is_moderator              boolean,
  constraint pk_exoluser primary key (id))
;

create table vote (
  id                        bigserial not null,
  value                     integer,
  solution_id               bigint,
  exercise_id               bigint,
  user_id                   bigint,
  constraint pk_vote primary key (id))
;


create table exercise_tag (
  exercise_id                    bigint not null,
  tag_id                         bigint not null,
  constraint pk_exercise_tag primary key (exercise_id, tag_id))
;
alter table comment add constraint fk_comment_user_1 foreign key (user_id) references exoluser (id);
create index ix_comment_user_1 on comment (user_id);
alter table comment add constraint fk_comment_solution_2 foreign key (solution_id) references solution (id);
create index ix_comment_solution_2 on comment (solution_id);
alter table comment add constraint fk_comment_exercise_3 foreign key (exercise_id) references exercise (id);
create index ix_comment_exercise_3 on comment (exercise_id);
alter table exercise add constraint fk_exercise_user_4 foreign key (user_id) references exoluser (id);
create index ix_exercise_user_4 on exercise (user_id);
alter table report add constraint fk_report_user_5 foreign key (user_id) references exoluser (id);
create index ix_report_user_5 on report (user_id);
alter table report add constraint fk_report_solution_6 foreign key (solution_id) references solution (id);
create index ix_report_solution_6 on report (solution_id);
alter table report add constraint fk_report_exercise_7 foreign key (exercise_id) references exercise (id);
create index ix_report_exercise_7 on report (exercise_id);
alter table report add constraint fk_report_comment_8 foreign key (comment_id) references comment (id);
create index ix_report_comment_8 on report (comment_id);
alter table solution add constraint fk_solution_exercise_9 foreign key (exercise_id) references exercise (id);
create index ix_solution_exercise_9 on solution (exercise_id);
alter table solution add constraint fk_solution_user_10 foreign key (user_id) references exoluser (id);
create index ix_solution_user_10 on solution (user_id);
alter table track add constraint fk_track_tag_11 foreign key (tag_id) references tag (id);
create index ix_track_tag_11 on track (tag_id);
alter table track add constraint fk_track_user_12 foreign key (user_id) references exoluser (id);
create index ix_track_user_12 on track (user_id);
alter table vote add constraint fk_vote_solution_13 foreign key (solution_id) references solution (id);
create index ix_vote_solution_13 on vote (solution_id);
alter table vote add constraint fk_vote_exercise_14 foreign key (exercise_id) references exercise (id);
create index ix_vote_exercise_14 on vote (exercise_id);
alter table vote add constraint fk_vote_user_15 foreign key (user_id) references exoluser (id);
create index ix_vote_user_15 on vote (user_id);



alter table exercise_tag add constraint fk_exercise_tag_exercise_01 foreign key (exercise_id) references exercise (id);

alter table exercise_tag add constraint fk_exercise_tag_tag_02 foreign key (tag_id) references tag (id);

# --- !Downs

drop table if exists comment cascade;

drop table if exists exercise cascade;

drop table if exists exercise_tag cascade;

drop table if exists report cascade;

drop table if exists solution cascade;

drop table if exists tag cascade;

drop table if exists track cascade;

drop table if exists exoluser cascade;

drop table if exists vote cascade;

