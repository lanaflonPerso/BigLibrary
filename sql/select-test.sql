CONNECT 'jdbc:derby://localhost:1527/librarydb;user=test;password=test';

SELECT * FROM library_card_items;
SELECT * FROM catalog_items;
SELECT * FROM library_cards;
SELECT * FROM statuses;
SELECT * FROM users;
SELECT * FROM roles;
SELECT * FROM users_blocked;

--SELECT SUM(publication_year) FROM catalog_items GROUP BY title;
--(COUNT, MIN, MAX, AVG è SUM)
--pstmt.setMaxRows(5);
--SELECT author, MAX(instances_number) FROM catalog_items GROUP BY author;

DISCONNECT;