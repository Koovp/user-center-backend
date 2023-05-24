create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    username      varchar(256)             null comment '用户昵称',
    user_account  varchar(256)             null comment '用户账号',
    avatar_url    varchar(1024)            null comment '用户头像',
    gender        tinyint                  null comment '性别',
    user_password varchar(512)             not null comment '密码',
    phone         varchar(128)             null comment '电话',
    email         varchar(512)             null comment '邮箱',
    user_role     int      default 0       not null comment '用户角色 0 - 普通用户 1 - 管理员',
    user_status   int      default 0       not null comment '用户状态 0 - 正常 1 - 失效',
    create_time   datetime default (now()) null comment '创建时间',
    update_time   datetime                 null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted       tinyint  default 0       null comment '是否删除 0 - 未删除'
)
    comment '用户';