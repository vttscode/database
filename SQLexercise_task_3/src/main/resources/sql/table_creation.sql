drop table if exists Timesheet;
drop table if exists Employee;
drop table if exists Currency;

CREATE TABLE Currency
(
  name VARCHAR(4) PRIMARY KEY,
  rate DOUBLE
);

CREATE TABLE Employee
(
  id          INT(10) PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(40),
  hourly      DOUBLE,
  position    varchar(30),
  Currency    varchar(4),
  on_vacation BOOLEAN,
  FOREIGN KEY (Currency) REFERENCES Currency (name)
);

CREATE TABLE Timesheet
(
  employee_id INT(10) PRIMARY KEY,
  hours       DOUBLE,
  FOREIGN KEY (employee_id) REFERENCES Employee (id)
);

insert into Currency (name, rate)
values ('EUR', 1);
insert into Currency (name, rate)
values ('USD', 0.89);
insert into Currency (name, rate)
values ('RUB', 0.013);

insert into Employee (name, hourly, position, currency, on_vacation)
values ('Jonas', 24.0, 'developer', 'USD', false);
insert into Employee (name, hourly, position, currency, on_vacation)
values ('Ieva', 24.0, 'HR', 'EUR', true);
insert into Employee (name, hourly, position, currency, on_vacation)
values ('Mantas', 1903.4, 'HR', 'RUB', false);
insert into Employee (name, hourly, position, currency, on_vacation)
values ('Egle', 21.0, 'ceo', 'USD', false);
insert into Employee (name, hourly, position, currency, on_vacation)
values ('Baxteris', 0.99, 'office dog', 'EUR', false);
insert into Employee (name, hourly, position, currency, on_vacation)
values ('Matas', 45.0, 'developer', 'EUR', true);
insert into Employee (name, hourly, position, currency, on_vacation)
values ('Matew', 36.0, 'developer', 'USD', false);

insert into Timesheet(employee_id, hours)
values (7, 34);
insert into Timesheet(employee_id, hours)
values (6, 0);
insert into Timesheet(employee_id, hours)
values (5, 154);
insert into Timesheet(employee_id, hours)
values (4, 90);
insert into Timesheet(employee_id, hours)
values (3, 10.5);
insert into Timesheet(employee_id, hours)
values (2, 34);
insert into Timesheet(employee_id, hours)
values (1, 76);