INSERT INTO tag VALUES (8000, 'Basic Knowledge', true);
INSERT INTO tag VALUES (8001, 'ZZZ boring', true);
INSERT INTO tag VALUES (8002, 'AAA nice', true);
INSERT INTO tag VALUES (8003, 'ABC', false);
INSERT INTO tag VALUES (8004, 'AAC' false)
INSERT INTO exoluser VALUES (8000, 'Franz', NULL, 12, NULL);
INSERT INTO exoluser VALUES (8001, 'Hans', NULL, -5, NULL);
INSERT INTO exercise VALUES (8000, 'Whats 1+2?', NULL, 5, 'Basic Math', 8000);
INSERT INTO exercise VALUES (8001, 'What''s the opposite of false?', NULL, -2, 'Basic Logic', 8000);
INSERT INTO exercise_tag VALUES (8000, 8000);
INSERT INTO exercise_tag VALUES (8001, 8000);
INSERT INTO solution VALUES (8000, 'It''s obviously 4', NULL, -12, NULL, 8000, 8001);
INSERT INTO solution VALUES (8001, 'It''s 3.', NULL, 3, NULL, 8000, 8000);
INSERT INTO solution VALUES (8002, 'It is true.', NULL, 3, NULL, 8001, 8001);
INSERT INTO solution VALUES (8003, 'It is very true...', NULL, 1, NULL, 8001, 8000);
INSERT INTO track VALUES (8000, 8000, 8000);
INSERT INTO comment VALUES (8000, 'Yes, yes i agree very much.', 8001, 8001, NULL, NULL);
INSERT INTO comment VALUES (8001, 'HOW CAN YOU BE SO WRONG!', 8000, 8000, NULL, NULL);
