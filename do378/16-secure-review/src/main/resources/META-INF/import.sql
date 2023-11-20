CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;
insert into Speaker (id, uuid, nameFirst, nameLast) values (nextval('hibernate_sequence'), 's-1-1', 'Emmanuel', 'Bernard');
insert into Speaker (id, uuid, nameFirst, nameLast) values (nextval('hibernate_sequence'), 's-1-2', 'Clement', 'Escoffier');
insert into Speaker (id, uuid, nameFirst, nameLast) values (nextval('hibernate_sequence'), 's-1-3', 'Alex', 'Soto');
insert into Speaker (id, uuid, nameFirst, nameLast) values (nextval('hibernate_sequence'), 's-1-4', 'Burr', 'Sutter');
