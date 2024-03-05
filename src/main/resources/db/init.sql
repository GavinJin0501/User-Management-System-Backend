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

create table user_management_system.tag
(
    id           bigint auto_increment comment 'id'
        primary key,
    tag_name     varchar(256)                       null comment 'Tag name',
    user_id      bigint                             null comment 'User id',
    parent_id    bigint                             null comment 'Parent tag id',
    is_parent    tinyint                            null comment '0: is not a parent tag, 1: is a parent tag',
    created_time datetime default CURRENT_TIMESTAMP not null comment 'Account created time',
    updated_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'Account updated time',
    is_deleted   tinyint  default 0                 not null comment 'Account is deleted logically',
    constraint uniIdx_tagName
        unique (tag_name)
)
    comment 'Tag table';

create index idx_userId
    on user_management_system.tag (user_id);


create table user_management_system.team
(
    id           bigint auto_increment comment 'id'
        primary key,
    name         varchar(256)                       not null comment 'Team name',
    description  varchar(1024)                      null comment 'Team description',
    max_num      int      default 1                 not null comment 'Max team size',
    expiration   datetime                           null comment 'Expiration time',
    userId       bigint                             not null comment 'Team creator id',
    status       int      default 0                 not null comment '0: public, 1: private, 2: password',
    password     varchar(512)                       null comment 'Password to enter the team (if any)',
    created_time datetime default CURRENT_TIMESTAMP not null comment 'Account created time',
    updated_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'Account updated time',
    is_deleted   tinyint  default 0                 not null comment 'Account is deleted logically'
)
    comment 'Team table';


create table user_management_system.user_team
(
    id           bigint auto_increment comment 'id'
        primary key,
    userId       bigint                             null comment 'User id in the team',
    teamId       bigint                             null comment 'Team id',
    joined_time  datetime                           null comment 'Time of join in the team',
    created_time datetime default CURRENT_TIMESTAMP not null comment 'Account created time',
    updated_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'Account updated time',
    is_deleted   tinyint  default 0                 not null comment 'Account is deleted logically'
)
    comment 'User-Team relation table';

