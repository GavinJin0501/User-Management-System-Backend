DROP TABLE IF EXISTS user;

create table user (
    id            bigint auto_increment  primary key      comment 'id',
    username      varchar(256)                       null comment 'User nickname',
    user_account  varchar(256)                       null comment 'User account',
    avatar_url    varchar(1024)                      null comment 'Url of avatar',
    gender        tinyint                            null comment 'Gender',
    user_password varchar(512)                       not null comment 'User password',
    phone         varchar(128)                       null comment 'Phone number',
    email         varchar(512)                       null comment 'User email',
    user_status   int      default 0                 not null comment 'Account status: default is 0 - normal',
    created_time  datetime default CURRENT_TIMESTAMP not null comment 'Account created time',
    updated_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'Account updated time',
    is_deleted    tinyint  default 0                 not null comment 'Account is deleted logically'
) comment 'User table';
