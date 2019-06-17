drop table if exists Salary;
drop table if exists Person;
create table Person
(
  ID   INT PRIMARY KEY AUTO_INCREMENT,
  NAME VARCHAR(30),
  AGE  INT(5)
);

create table Salary
(
  person_id int PRIMARY KEY,
  pay       int(10),
  FOREIGN KEY (person_id) REFERENCES Person (id)
);

insert into person(id, name, age)
values (1, 'Jonas', 10);
insert into person(id, name, age)
values (2, 'Tomas', 53);
insert into person(id, name, age)
values (3, 'Egle', 22);
insert into person(id, name, age)
values (4, 'Toma', 33);
insert into person(id, name, age)
values (5, 'Ieva', 22);

insert into salary(person_id, pay)
values (1, 1000);
insert into salary(person_id, pay)
values (2, 2500);
insert into salary(person_id, pay)
values (3, 0);
insert into salary(person_id, pay)
values (5, 1234);