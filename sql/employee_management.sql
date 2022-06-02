create table employee
(
    employee_id           varchar(36) not null
        primary key,
    employee_name         varchar(45) not null,
    employee_sex          varchar(1)  not null,
    employee_age          varchar(2)  not null,
    employee_id_card      varchar(18) not null,
    employee_address      varchar(45) not null,
    employee_phone_number varchar(11) not null,
    created_by            varchar(45) not null,
    created_date          varchar(19) not null,
    last_modified_date    varchar(19) not null,
    constraint employee_employee_id_uindex
        unique (employee_id)
)
    comment '员工信息表';

create table operation_logs
(
    log_id                varchar(36) not null
        primary key,
    dml                   varchar(6)  not null,
    employee_id           varchar(36) not null,
    employee_name         varchar(45) not null,
    employee_sex          varchar(1)  not null,
    employee_age          varchar(2)  not null,
    employee_id_card      varchar(18) not null,
    employee_address      varchar(45) not null,
    employee_phone_number varchar(11) not null,
    created_by            varchar(45) not null,
    created_date          varchar(19) not null,
    last_modified_date    varchar(19) not null,
    username              varchar(45) not null,
    date_time             varchar(19) not null,
    constraint employee_management_operation_logs_log_id_uindex
        unique (log_id)
)
    comment '员工管理操作日志表';

create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null
        primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);

create table search_record
(
    record_id       varchar(36) null,
    search_group_by varchar(45) null,
    record_name     varchar(45) null,
    username        varchar(45) null,
    created_date    datetime    null
)
    comment '搜索记录表';

create table user
(
    user_id     varchar(36) not null
        primary key,
    username    varchar(45) not null,
    password    varchar(60) not null,
    authorities varchar(45) not null,
    constraint user_user_id_uindex
        unique (user_id)
)
    comment '用户表';