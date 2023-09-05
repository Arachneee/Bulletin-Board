drop table if exists post CASCADE;
create table post
(
    id bigint generated by default as identity,
    title varchar(100),
    content longtext,
    member_id bigint,
    create_time datetime,
    view_count int,
    foreign key (member_id) references member(id) on update cascade,
    primary key (post_id)
);