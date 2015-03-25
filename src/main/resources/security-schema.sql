CREATE TABLE users (username varchar(50) not null primary key,password varchar(50) not null,enabled char not null);
CREATE TABLE authorities (username varchar(50) not null,authority varchar(50) not null);

INSERT INTO users VALUES ( 'admin', 'secure', '1' );
INSERT INTO users VALUES ( 'badal', 'pass1', '1' );
INSERT INTO users VALUES ( 'test', 'test', '1' );
INSERT INTO users VALUES ( 'abid', 'pass2', '1' );
INSERT INTO users VALUES ( 'ranjan', 'pass3', '1' );

INSERT INTO authorities(username, authority) values ('admin','ROLE_ADMIN');
INSERT INTO authorities(username, authority) values ('admin','admin');
INSERT INTO authorities(username, authority) values ('badal','accounts');
INSERT INTO authorities(username, authority) values ('badal','marketing');
INSERT INTO authorities(username, authority) values ('abid','accounts');
INSERT INTO authorities(username, authority) values ('ranjan','marketing');
INSERT INTO authorities(username, authority) values ('test','users');