CONNECT 'jdbc:derby://localhost:1527/librarydb;create=true;user=test;password=test';
DROP TABLE library_card_items;
DROP TABLE catalog_items;
DROP TABLE library_cards;
DROP TABLE users_blocked;
DROP TABLE statuses;
DROP TABLE users;
DROP TABLE roles;
DROP TABLE locales;
DROP TRIGGER check_user;
DROP FUNCTION fire;

----------------------------------------------------
CREATE TABLE roles(
	id INTEGER NOT NULL PRIMARY KEY,
	name VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO roles VALUES(0, 'admin');
INSERT INTO roles VALUES(1, 'reader');
INSERT INTO roles VALUES(2, 'librarian');
----------------------------------------------------
CREATE TABLE locales(
	id INTEGER NOT NULL generated always AS identity PRIMARY KEY,
	name VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO locales VALUES(DEFAULT, 'en');
INSERT INTO locales VALUES(DEFAULT, 'ru');
----------------------------------------------------
CREATE TABLE users(
	id INTEGER NOT NULL generated always AS identity PRIMARY KEY,
	login VARCHAR(20) NOT NULL UNIQUE,
	password VARCHAR(128) NOT NULL,
	first_name VARCHAR(20) NOT NULL,
	last_name VARCHAR(20) NOT NULL,
	email VARCHAR(30) NOT NULL UNIQUE,
	locale_id INTEGER NOT NULL REFERENCES locales(id) DEFAULT 1,
	books_has_taken INTEGER NOT NULL DEFAULT 0,
	role_id INTEGER NOT NULL REFERENCES roles(id) 
		ON DELETE CASCADE 
		ON UPDATE RESTRICT
);

INSERT INTO users VALUES(DEFAULT, 'admin', 'C7AD44CBAD762A5DA0A452F9E854FDC1E0E7A52A38015F23F3EAB1D80B931DD472634DFAC71CD34EBC35D16AB7FB8A90C81F975113D6C7538DC69DD8DE9077EC', 'Ivan', 'Ivanov', 'admin@gmail.ru', DEFAULT, DEFAULT, 0);
INSERT INTO users VALUES(DEFAULT, 'reader', '2D7349C51A3914CD6F5DC28E23C417ACE074400D7C3E176BCF5DA72FDBEB6CE7ED767CA00C6C1FB754B8DF5114FC0B903960E7F3BEFE3A338D4A640C05DFAF2D', 'Petr', 'Petrov', 'reader@gmail.ru', 2, DEFAULT, 1);
INSERT INTO users VALUES(DEFAULT, '73286B453EC71C69968D377FF3D5F6F58410EDF01F185E55170A6110A3616ED1417BB6D33754058D3652F70180A0D5A5C5E8F141C1B8A098556A245DE8D9221F', 'librarian', 'Maria', 'Pavlovna', 'librairan@gmail.ru', 2, DEFAULT, 2);
----------------------------------------------------
CREATE TABLE statuses(
	id INTEGER NOT NULL PRIMARY KEY,
	name VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO statuses VALUES(0, 'reading room');
INSERT INTO statuses VALUES(1, 'library_card');
INSERT INTO statuses VALUES(2, 'closed');
INSERT INTO statuses VALUES(3, 'not confirmed');

CREATE TABLE users_blocked(
	id INTEGER NOT NULL generated always AS identity PRIMARY KEY,
	user_id INTEGER NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE 
);
----------------------------------------------------
CREATE TABLE library_cards(
	id INTEGER NOT NULL generated always AS identity PRIMARY KEY,
	user_id INTEGER NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE 
);
--==============================================================================
-- user defined function
CREATE FUNCTION fire(message VARCHAR(30))
RETURNS BOOLEAN
PARAMETER STYLE Java
NO SQL LANGUAGE JAVA 
EXTERNAL NAME 'ua.khai.slynko.Library.exception.DBException.fire';
--==============================================================================
-- trigger
CREATE TRIGGER check_user
NO CASCADE BEFORE INSERT ON library_cards
REFERENCING NEW AS inserted
FOR EACH ROW
SELECT fire('Not a reader') FROM users WHERE users.id = inserted.user_id AND users.role_id != 1;


INSERT INTO library_cards VALUES(DEFAULT, 2);
INSERT INTO library_cards VALUES(DEFAULT, 1); --- attention here sould be an exception !!!!!
----------------------------------------------------
CREATE TABLE catalog_items (
	id INTEGER NOT NULL generated always AS identity PRIMARY KEY,
	title VARCHAR(50) NOT NULL,
	author VARCHAR(50) NOT NULL,
	edition VARCHAR(50) NOT NULL,
	publication_year INTEGER NOT NULL,
	instances_number INTEGER DEFAULT 0
);

INSERT INTO catalog_items VALUES(DEFAULT, 'Atlas Shrugged', 'Ayn Rand', 'Cheap edition', 1996, 3);
INSERT INTO catalog_items VALUES(DEFAULT, 'The Art Of War', 'Sun Tzu', 'Book club edition', 2007, 2);
INSERT INTO catalog_items VALUES(DEFAULT, 'The Light and Truth of Slavery', 'Aaron', 'Paperback edition', 1845, 10);
INSERT INTO catalog_items VALUES(DEFAULT, 'Message from the President of the United States', 'Thomas Jefferson', 'Library edition', 2005, 5);
----------------------------------------------------              
CREATE TABLE library_card_items (
	id INTEGER NOT NULL generated always AS identity PRIMARY KEY,
	library_card_id INTEGER NOT NULL REFERENCES library_cards(id),
	catalog_item_id INTEGER NOT NULL REFERENCES catalog_items(id) ON DELETE CASCADE ,
	date_from DATE,
	date_to DATE,
	penalty_size INTEGER,
	status_id INTEGER REFERENCES statuses(id) DEFAULT 3
);

INSERT INTO library_card_items VALUES(DEFAULT, 1, 2, '2016-01-01', '2016-01-27', 100, 1);
INSERT INTO library_card_items VALUES(DEFAULT, 1, 2, '2016-01-01', '2016-10-20', 150, 1);
INSERT INTO library_card_items VALUES(DEFAULT, 1, 1, DEFAULT, DEFAULT, DEFAULT, 3);
----------------------------------------------------
SELECT * FROM library_card_items;
SELECT * FROM catalog_items;
SELECT * FROM library_cards;
SELECT * FROM users_blocked;
SELECT * FROM statuses;
SELECT * FROM users;
SELECT * FROM roles;
SELECT * FROM locales;

DISCONNECT;