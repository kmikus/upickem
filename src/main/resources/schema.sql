drop table if exists login;

create table login(
	username varchar(20) primary key,
	password varchar(20) not null
);