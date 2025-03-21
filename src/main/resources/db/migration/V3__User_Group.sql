CREATE TABLE IF NOT EXISTS USER_GROUP (
       id uuid not null,
       user_id uuid not null,
       group_id uuid not null,
       constraint pk_user_group primary key (user_id, group_id)
);

create index ix_user_group_user on USER_GROUP(user_id);
alter table USER_GROUP add constraint fk_user_group_user foreign key (user_id) references USERS(id) on delete restrict on update restrict;
create index ix_user_group_group on USER_GROUP(group_id);
alter table USER_GROUP add constraint fk_user_group_group foreign key (group_id) references GROUPS(id) on delete restrict on update restrict;
