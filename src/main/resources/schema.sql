drop all objects;

create table if not exists items
(
    item_id          INTEGER auto_increment,
    item_name        CHARACTER VARYING(200) not null,
    item_description CHARACTER VARYING(200) not null,
    item_available   BOOLEAN                not null,
    constraint item_id
        primary key (item_id)
);

create table if not exists users
(
    user_id    INTEGER auto_increment,
    user_name  CHARACTER VARYING(50),
    user_email CHARACTER VARYING(200) not null,
    constraint user_id
        primary key (user_id)
);