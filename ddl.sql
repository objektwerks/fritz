drop schema public cascade;
create schema public;

create table accounts (
    id bigserial,
    license varchar(36) not null,
    pin varchar(7) not null,
    email varchar(128) not null,
    created bigint not null,
    constraint account_pk primary key (id)
);
alter table accounts add constraint license_idx unique (license);

create table pools (
    id bigserial,
    license varchar(36) not null,
    "name" varchar(128) not null,
    volume int not null,
    uom varchar(6) not null,
    constraint pool_pk primary key (id)
);
create index name_idx on pools ("name" asc);

create table cleanings (
    id bigserial,
    pool_id bigint not null,
    brush boolean not null,
    net boolean not null,
    skimmer_basket boolean not null,
    pump_basket boolean not null,
    pump_filter boolean not null,
    "vacuum" boolean not null,
    cleaned bigint not null,
    constraint cleaning_pk primary key (id),
    constraint fk_cleanings_pool_id__id foreign key (pool_id) references pools(id) on delete restrict on update restrict
);
create index cleaned_idx on cleanings (cleaned desc);

create table measurements (
    id bigserial,
    pool_id bigint not null,
    total_chlorine int not null,
    free_chlorine int not null,
    combined_chlorine double precision not null,
    ph double precision not null,
    calcium_hardness int not null,
    total_alkalinity int not null,
    cyanuric_acid int not null,
    total_bromine int not null,
    salt int not null,
    temperature int not null,
    measured bigint not null,
    constraint measurement_pk primary key (id),
    constraint fk_measurements_pool_id__id foreign key (pool_id) references pools(id) on delete restrict on update restrict
);
create index measured_idx on measurements (measured desc);

create table chemicals (
    id bigserial,
    pool_id bigint not null,
    additive varchar(16) not null,
    amount double precision not null,
    uom varchar(6) not null,
    added bigint not null,
    constraint chemical_pk primary key (id),
    constraint fk_chemicals_pool_id__id foreign key (pool_id) references pools(id) on delete restrict on update restrict
);
create index added_idx on chemicals (added desc);

create table faults (
  cause varchar(256) not null,
  occurred bigint not null
);
create index occurred_idx on faults (occurred desc);