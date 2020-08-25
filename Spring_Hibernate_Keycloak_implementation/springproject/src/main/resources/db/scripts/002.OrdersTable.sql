create table orders
(
    id       serial  not null
        constraint orders_pk
            primary key,
    user_id  integer not null
        constraint orders_users_id_fk
            references users,
    quantity integer,
    info     varchar(256),
    status   varchar(255)
);

alter table orders
    owner to postgres;

create unique index orders_id_uindex
    on orders (id);

