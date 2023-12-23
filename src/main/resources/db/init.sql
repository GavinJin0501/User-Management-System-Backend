DROP TABLE IF EXISTS user;

create table user_management_system.user
(
    id            bigint auto_increment comment 'id'
        primary key,
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
    is_deleted    tinyint  default 0                 not null comment 'Account is deleted logically',
    user_role     int      default 0                 not null comment 'Role of user: 0 - Normal User, 1 - Administrator, 2 - VIP',
    planet_code   varchar(512)                       null comment 'Code of the planet'
)
    comment 'User table';


