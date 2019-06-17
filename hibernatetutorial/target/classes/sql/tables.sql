drop table if exists Contact;
drop table if exists Employee;
drop table if exists Branch;
drop table if exists Company;
drop table if exists Address;
drop table if exists Expense;

create table Address
(
  id     int primary key AUTO_INCREMENT,
  street varchar(40) unique,
  city   varchar(30)
);
create table Company
(
  id      int primary key AUTO_INCREMENT,
  name    varchar(40) UNIQUE,
  address int,
  CONSTRAINT fk_company_address FOREIGN KEY (address) REFERENCES Address (id)
);
create table Branch
(
  id      int primary key AUTO_INCREMENT,
  name    VARCHAR(30),
  company int,
  CONSTRAINT fk_company_branch FOREIGN KEY (company) REFERENCES Company (id)
);
create table Employee
(
  id       int primary key AUTO_INCREMENT,
  name     varchar(40) UNIQUE,
  position varchar(30),
  company  int,
  address  int,
  CONSTRAINT fk_Employee_address FOREIGN KEY (address) REFERENCES Address (id),
  CONSTRAINT fk_Company FOREIGN KEY (company) REFERENCES Company (id)
);
create table Contact
(
  id           int primary key AUTO_INCREMENT,
  ref_id       int,
  contact_type varchar(30),
  value        varchar(40),
  CONSTRAINT fk_contact_Person FOREIGN KEY (ref_id) REFERENCES Employee (id)
);
create table Expense
(
  id          int primary key AUTO_INCREMENT,
  name        VARCHAR(30),
  description VARCHAR(100),
  date        DATE
);


insert into Address(street, city)
values ('Gedo pr.', 'Vilnius');
insert into Address(street, city)
values ('Laisves al.', 'Kaunas');
insert into Company(name, address)
values ('Microsoft', 1);
insert into Employee(name, position, company, address)
values ('Jonas', 'developer', 1, 2);
insert into Branch (name, company)
values ('Vilnius HQ', 1);
insert into Contact (ref_id, contact_type, value)
values (1, 'mobile phone', '370');
insert into Expense(name, description, date)
values ('office party \'19', 'second annual get together of employees', DATE '2019-03-17');
insert into Expense(name, description, date)
values ('office party \'18', 'first annual get together of employees', DATE '2018-03-24');