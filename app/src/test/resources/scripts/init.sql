create table if not exists gift_certificate
(
    id bigint auto_increment
        primary key,
    name tinytext null,
    description text null,
    price decimal null,
    duration int null,
    create_date datetime null,
    last_update_date datetime null
);

create table if not exists tag
(
    id bigint auto_increment
        primary key,
    name tinytext null
);

create table if not exists gift_certificate_has_tag
(
    gift_certificate_id bigint not null,
    tag_id bigint not null,
    constraint gift_certificate_has_tag_gift_certificate_id_fk
        foreign key (gift_certificate_id) references gift_certificate (id)
            on update cascade on delete cascade,
    constraint gift_certificate_has_tag_tag_id_fk
        foreign key (tag_id) references tag (id)
            on update cascade on delete cascade
);

