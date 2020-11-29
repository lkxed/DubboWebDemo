create table account(id int primary key auto_increment, username varchar(20) unique, balance decimal(5, 2) not null default 0);

insert into account(username, balance) values ('1001', 20);
insert into account(username, balance) values ('1002', 20);