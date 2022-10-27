drop
all objects;

create table if not exists users
(
    user_id    INTEGER auto_increment,
    user_name  CHARACTER VARYING(50)  not null,
    user_email CHARACTER VARYING(200) not null,
    constraint user_pk primary key (user_id),
    CONSTRAINT user_email_unique UNIQUE (user_email)
);

create table if not exists requests
(
    request_id   INTEGER auto_increment,
    description  CHARACTER VARYING(200) not null,
    created      DATETIME               not null,
    requester_id INTEGER                not null,
    constraint request_pk primary key (request_id),
    CONSTRAINT requester_fk FOREIGN key (requester_id) REFERENCES users ON DELETE CASCADE
);

create table if not exists items
(
    item_id          INTEGER auto_increment,
    item_name        CHARACTER VARYING(200) not null,
    item_description CHARACTER VARYING(200) not null,
    item_available   BOOLEAN                not null,
    user_id          INTEGER                not null,
    request_id       INTEGER,
    constraint item_id primary key (item_id),
    CONSTRAINT item_request_fk FOREIGN key (request_id) REFERENCES requests on delete set null,
    CONSTRAINT item_user_fk FOREIGN key (user_id) REFERENCES users ON DELETE CASCADE

);

create table if not exists bookings
(
    booking_id INTEGER auto_increment,
    start_date DATETIME                                            not null,
    end_date   DATETIME                                            not null,
    status     ENUM('WAITING', 'APPROVED', 'CANCELED', 'REJECTED') not null,
    item_id    INTEGER                                             not null,
    booker_id  INTEGER                                             not null,
    constraint booking_pk primary key (booking_id),
    CONSTRAINT item_fk FOREIGN key (item_id) REFERENCES items ON DELETE CASCADE,
    CONSTRAINT booker_fk FOREIGN key (booker_id) REFERENCES users ON DELETE CASCADE
);

create table if not exists comments
(
    comment_id   INTEGER auto_increment,
    comment_text CHARACTER VARYING(500) not null,
    created      DATETIME               not null,
    item_id      INTEGER                not null,
    author_id    INTEGER                not null,
    constraint comment_pk primary key (comment_id),
    CONSTRAINT item_comment_fk FOREIGN key (item_id) REFERENCES items ON DELETE CASCADE,
    CONSTRAINT author_fk FOREIGN key (author_id) REFERENCES users ON DELETE CASCADE
);