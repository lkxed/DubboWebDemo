create table merchandise(id int primary key auto_increment, name varchar(20) unique, price double not null, inventory int not null default 0);

insert into merchandise(name, price, inventory) values ('iPhone 11', 3999, 23);
insert into merchandise(name, price, inventory) values ('Apple Watch S5', 2699, 45);