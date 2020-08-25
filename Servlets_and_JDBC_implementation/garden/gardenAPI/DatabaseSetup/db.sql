DROP TABLE IF EXISTS roles,users,orderTypes,orders;
DROP SEQUENCE IF EXISTS id_seq,ordertypes_id_seq,orders_id_seq;

CREATE SEQUENCE IF NOT EXISTS id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

create table if not exists roles
(
    id   serial      NOT NULL primary key,
    name varchar(10) NOT NULL
);

insert into roles
values (1, 'admin');
insert into roles
values (2, 'gardener');
insert into roles
values (3, 'owner');


create table IF NOT EXISTS users
(
    id       serial             NOT NULL primary key,
    login    varchar(32) UNIQUE NOT NULL,
    password varchar(32)        NOT NULL,
    name     varchar            NOT NULL,
    role     INTEGER            NOT NULL,
    foreign key (role) references roles (id)
);

ALTER TABLE users
    ALTER COLUMN id SET default nextval('id_seq');

insert into users
values (nextval('id_seq'), 'admin', 'admin', 'Mike', 1);
insert into users
values (nextval('id_seq'), 'gardener', 'gardener', 'David', 2);
insert into users
values (nextval('id_seq'), 'owner', 'owner', 'Alice', 3);

create table if not exists orderTypes
(
    id   serial NOT NULL primary key,
    name varchar(20)
);
CREATE SEQUENCE IF NOT EXISTS ordertypes_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

ALTER SEQUENCE ordertypes_id_seq
    OWNED BY orderTypes.id;
ALTER TABLE orderTypes
    ALTER COLUMN id SET default nextval('ordertypes_id_seq');

insert into orderTypes
values (nextval('ordertypes_id_seq'), 'heal');
insert into orderTypes
values (nextval('ordertypes_id_seq'), 'art');
insert into orderTypes
values (nextval('ordertypes_id_seq'), 'destroy');

create table if not exists orders
(
    id        serial NOT NULL primary key,
    owner     integer,
    gardener  integer,
    orderType integer,
    quantity  integer,
    finished  boolean,
    approved  boolean,
    foreign key (owner) references users (id),
    foreign key (gardener) references users (id),
    foreign key (orderType) references orderTypes (id)
);

CREATE SEQUENCE IF NOT EXISTS orders_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

ALTER SEQUENCE orders_id_seq
    OWNED BY orders.id;
ALTER TABLE orders
    ALTER COLUMN id SET default nextval('orders_id_seq');

insert into orders
values (nextval('orders_id_seq'),
        (SELECT users.id from users where users.name = 'owner'),
        (SELECT users.id from users where users.name = 'gardener'),
        1, 10, false, false);
insert into orders
values (nextval('orders_id_seq'),
        (SELECT users.id from users where users.name = 'owner'),
        (SELECT users.id from users where users.name = 'gardener'),
        1, 10, false, true);
insert into orders
values (nextval('orders_id_seq'),
        (SELECT users.id from users where users.name = 'owner'),
        (SELECT users.id from users where users.name = 'gardener'),
        1, 10, true, false);
insert into orders
values (nextval('orders_id_seq'),
        (SELECT users.id from users where users.name = 'owner'),
        (SELECT users.id from users where users.name = 'gardener'),
        1, 10, true, true);