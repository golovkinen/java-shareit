drop all objects;

create table if not exists ITEMS
(
    ITEM_ID          INTEGER auto_increment,
    ITEM_NAME        CHARACTER VARYING(200) not null,
    ITEM_DESCRIPTION CHARACTER VARYING(200) not null,
    ITEM_AVAILABLE   CHARACTER VARYING(50) not null,
    constraint ITEM_ID
        primary key (ITEM_ID)
);

create table if not exists USERS
(
    USER_ID    INTEGER auto_increment,
    USER_NAME  CHARACTER VARYING(50),
    USER_EMAIL CHARACTER VARYING(200) not null,
    constraint USER_ID
        primary key (USER_ID)
);