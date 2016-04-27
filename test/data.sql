SET client_encoding = 'UTF8';
--Track
INSERT INTO tag VALUES (8000, 'An1I', true);
INSERT INTO tag VALUES (8001, 'An2I', true);
INSERT INTO tag VALUES (8002, 'AD1', true);
INSERT INTO tag VALUES (8003, 'WED1', true);
INSERT INTO tag VALUES (8004, 'AD2', true);
INSERT INTO tag VALUES (8005, 'Tree', false);
INSERT INTO tag VALUES (8006, 'Pattern', false);
INSERT INTO tag VALUES (8007, 'SE1', true);
INSERT INTO tag VALUES (8008, 'SE2', true);
INSERT INTO tag VALUES (8009, 'Testing', false);
INSERT INTO tag VALUES (8010, 'simple operations', false);
INSERT INTO tag VALUES (8011, 'DMI', true);
INSERT INTO tag VALUES (8012, 'Logik', false);
INSERT INTO tag VALUES (8013, 'Mathematics', false);
INSERT INTO tag VALUES (8014, 'Software Engineering', false);
INSERT INTO tag VALUES (8015, 'VSS', true);
INSERT INTO tag VALUES (8016, 'APF', true);
INSERT INTO tag VALUES (8017, 'Integral', false);
INSERT INTO tag VALUES (8018, 'Ableiten', false);
INSERT INTO tag VALUES (8019, 'Clean Code', false);
INSERT INTO tag VALUES (8020, 'Error Handling', false);
INSERT INTO tag VALUES (8021, 'Funktion', false);
INSERT INTO tag VALUES (8022, 'Allgemein', true);
--Exoluser
INSERT INTO exoluser VALUES (8000, 'Franz', 'franz@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8003, 'Simon', 'simon@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8005, 'Denarya', 'denarya@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8006, 'Silwyna', 'silwyna@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8007, 'Angela', 'angela@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8008, 'Tony', 'tony@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8009, 'Sandro', 'sandro@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8010, 'Mario', 'mario@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8012, 'Lorem Ipsum', 'lorem.ipsum@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8011, 'Marco', 'marco@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8001, 'Hans', 'hans@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8004, 'Katherina', 'katherina@hsr.ch', NULL, false);
INSERT INTO exoluser VALUES (8002, 'Blubberduck', 'blubberduck@hsr.ch', NULL, true);
--Exercise
INSERT INTO exercise VALUES (8004, 'Was ist der Nachteil von Quicksort?', '2016-02-17 12:03:12', 'Quicksort', 8003);
INSERT INTO exercise VALUES (8003, 'Welche Sortierungsmethode ist die Beste?', '2016-02-17 05:52:16', 'Sortieren', 8003);
INSERT INTO exercise VALUES (8001, 'What''s the opposite of false?', '2016-02-17 03:04:05', 'Basic Logic', 8000);
INSERT INTO exercise VALUES (8007, 'Was ist die erste Ableitung von y=5x?

y''=???', '2015-12-02 08:00:02', 'Ableitung 1a)', 8002);
INSERT INTO exercise VALUES (8011, 'Wie gross ist die Fläche der Funktion y=x^3 zwischen dem Punkt x1=-1 und x2=1?', '2016-01-14 16:03:23', 'Fläche einer Funktion', 8002);
INSERT INTO exercise VALUES (8012, 'Besuche meine super Website: http://www.iamhans.com', '2016-04-11 21:45:33', 'XXX', 8001);
INSERT INTO exercise VALUES (8008, 'Was bedeutet TDD genau?', '2016-04-01 09:24:24', 'TDD Definition', 8008);
INSERT INTO exercise VALUES (8009, 'Was ist ein Unit Test?', '2016-04-01 09:27:12', 'Unit Testing', 8008);
INSERT INTO exercise VALUES (8010, 'Was ist der Unterschied zwischen einem System Test und einem Unit Test?', '2016-04-02 14:07:12', 'System Test vs. Unit Test', 8005);
INSERT INTO exercise VALUES (8005, 'In einem Code gibt es diverse Stellen mit „Catch-All“ von Exceptions. Die Absicht dahinter ist es, die Software fehlertolerant zu halten.

DataSet	load()	{

try	{

				//	read	logic

}	catch (Exception	e) {

			return	null;

}

}

DataSet filter(DataSet	d)	{

		try	{

				//	filter	logic

		}	catch	(Exception	e) {

				return	null;

		}

}

void	save(DataSet	d)	{

		try	{

				//	save	to	file

		}	catch	(Exception	e) {

		}

}

void	controlLoop()	{

		while(…)	{

				//	wait	for	condition

				save(filter(load());

		}

}



a) Welche Probleme sehen Sie bei diesem Exception Handling Design?

b) Welche Design-Änderungen empfehlen Sie?', '2016-03-18 14:52:03', 'Error Handling Design', 8008);
INSERT INTO exercise VALUES (8006, 'Entwerfen Sie ein Error-Handling Policy für Ihr Engineering-Projekt. Legen Sie dabei fest, wie Sie Exceptions, Assertions und Logging verwenden wollen.

Falls Sie kein Engineering-Projekt haben, verwenden Sie das kleine Java Beispiel-Projekt ImageGallery aus der Vorlage.', '2016-03-18 15:02:14', 'Erro Handling Policy', 8008);
INSERT INTO exercise VALUES (8002, 'Wie teuer ist eine Suchoperation eines AVL-Trees mit n Elementen? In O-Notation', '2016-02-17 23:55:34', 'AVL-Tree Suchkosten', 8002);
INSERT INTO exercise VALUES (8000, 'Whats 1+2?', '2015-01-08 00:00:00', 'Basic Math', 8000);
--ExcerciseTag
INSERT INTO exercise_tag VALUES (8000, 8000);
INSERT INTO exercise_tag VALUES (8000, 8010);
INSERT INTO exercise_tag VALUES (8001, 8011);
INSERT INTO exercise_tag VALUES (8001, 8012);
INSERT INTO exercise_tag VALUES (8002, 8002);
INSERT INTO exercise_tag VALUES (8002, 8004);
INSERT INTO exercise_tag VALUES (8002, 8005);
INSERT INTO exercise_tag VALUES (8003, 8002);
INSERT INTO exercise_tag VALUES (8004, 8002);
INSERT INTO exercise_tag VALUES (8011, 8001);
INSERT INTO exercise_tag VALUES (8011, 8017);
INSERT INTO exercise_tag VALUES (8007, 8021);
INSERT INTO exercise_tag VALUES (8007, 8001);
INSERT INTO exercise_tag VALUES (8007, 8018);
INSERT INTO exercise_tag VALUES (8011, 8021);
INSERT INTO exercise_tag VALUES (8005, 8008);
INSERT INTO exercise_tag VALUES (8005, 8020);
INSERT INTO exercise_tag VALUES (8006, 8008);
INSERT INTO exercise_tag VALUES (8006, 8020);
INSERT INTO exercise_tag VALUES (8008, 8008);
INSERT INTO exercise_tag VALUES (8008, 8009);
INSERT INTO exercise_tag VALUES (8009, 8008);
INSERT INTO exercise_tag VALUES (8009, 8009);
INSERT INTO exercise_tag VALUES (8010, 8008);
INSERT INTO exercise_tag VALUES (8010, 8009);
INSERT INTO exercise_tag VALUES (8012, 8022);
INSERT INTO exercise_tag VALUES (8012, 8010);
INSERT INTO exercise_tag VALUES (8012, 8020);
INSERT INTO exercise_tag VALUES (8012, 8012);
INSERT INTO exercise_tag VALUES (8012, 8003);
--Solution
INSERT INTO solution VALUES (8013, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', '2016-04-03 00:00:00', false, 8005, 8012);
INSERT INTO solution VALUES (8014, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', '2016-04-03 00:00:00', false, 8007, 8012);
INSERT INTO solution VALUES (8001, 'It''s 3.', '2016-03-05 23:53:23', true, 8000, 8000);
INSERT INTO solution VALUES (8002, 'It is true.', '2016-03-27 15:07:47', false, 8001, 8001);
INSERT INTO solution VALUES (8003, 'It is very true...', '2016-04-02 07:44:10', false, 8001, 8000);
INSERT INTO solution VALUES (8004, 'HÃ¤ngt sehr von der Definiton von + ab. StandartmÃ¤ssig ist es 3.', '2015-02-22 22:22:22', false, 8000, 8002);
INSERT INTO solution VALUES (8005, 'log(n)', '2015-12-31 23:59:59', true, 8002, 8002);
INSERT INTO solution VALUES (8015, 'y''=5', '2015-12-04 00:00:00', true, 8008, 8002);
INSERT INTO solution VALUES (8016, '5', '2015-12-06 08:07:56', false, 8008, 8005);
INSERT INTO solution VALUES (8017, 'htttp://www.iamhans.com', '2015-04-11 21:45:56', true, 8012, 8001);
INSERT INTO solution VALUES (8007, 'a) Fehler bleiben unbemerkt, insbesondere auch Programmierfehler. Daher sind Folgefehler immanent:

Bei Fehler in load() und filter() wird trotzdem save() ausgeführt, was beispielsweise zu Schreiben

von leere Daten führen kann. Zudem wird das Testen der Methoden schwieriger und die

Wiederverwendbarkeit eingeschränkt, weil im Fehlerfall keine Exception an den Aufrufer propagiert

wird.

b) Empfohlene Änderungen:

• Fehler in Hilfsmethoden (load, filter, save) an Aufrufer propagieren.

• Abfangen von spezifischen Exception-Typen, die von externen Fehler stammen (z.B. IOExceptions).

• Bei Fehler in Teilmethode (z.B. in load()), weitere Aufrufe (z.B. save()) nicht ausführen.

• Loggen der aufgetretenen Fehler.', '2016-03-18 14:59:11', true, 8005, 8008);
INSERT INTO solution VALUES (8009, 'a) Fehler bleiben unbemerkt was problematisch ist.

b) Kein Catch-All benutzen', '2016-03-18 19:35:48', false, 8005, 8002);
INSERT INTO solution VALUES (8010, 'a) Kritische Fehler werden sozusagen einfach ignoriert.

b) Jeden Fehler einzeln abfangen (nach Typ) und jenachdem anders behandeln.', '2016-03-18 17:23:21', false, 8005, 8010);
INSERT INTO solution VALUES (8011, 'a) Ich sehe dabei nicht wirklich ein Problem.

b) Ich würde gar keine Änderung hier empfehlen.', '2016-03-20 09:04:29', false, 8005, 8007);
INSERT INTO solution VALUES (8008, 'a) kA

b) kA

Nur für die Lösung hier', '2016-03-19 05:59:23', false, 8005, 8011);
INSERT INTO solution VALUES (8006, '50CHF?', '2016-01-01 00:00:00', true, 8002, 8001);
INSERT INTO solution VALUES (8000, 'It''s obviously 4', '2016-03-05 19:55:23', false, 8000, 8001);
INSERT INTO solution VALUES (8012, 'http://www.iamhans.com', '2016-04-08 04:02:58', false, 8005, 8001);
--Track
INSERT INTO track VALUES (8000, 8000, 8000);
INSERT INTO track VALUES (8001, 8002, 8002);
INSERT INTO track VALUES (8002, 8004, 8002);
INSERT INTO track VALUES (8003, 8007, 8010);
INSERT INTO track VALUES (8004, 8008, 8010);
INSERT INTO track VALUES (8005, 8009, 8010);
INSERT INTO track VALUES (8006, 8012, 8006);
INSERT INTO track VALUES (8007, 8008, 8000);
--Comment
INSERT INTO comment VALUES (8000, 'Yes, yes i agree very much.', 8001, 8001, NULL, '2016-04-01 08:23:42');
INSERT INTO comment VALUES (8001, 'HOW CAN YOU BE SO WRONG!', 8000, 8000, NULL, '2016-03-27 14:52:19');
INSERT INTO comment VALUES (8002, 'Was heisst O-Notation? noch nie gehört davon..', 8001, NULL, 8002, '2016-04-07 17:55:12');
INSERT INTO comment VALUES (8003, 'Vielleicht wäre (was sind die Kosten) besser gewesen wie (Wie teuer ist)', 8004, NULL, 8002, '2016-04-07 18:12:55');
INSERT INTO comment VALUES (8004, 'Du hast auch keine Ahnung von O-Notation?', 8004, 8006, NULL, '2016-04-06 11:12:55');
INSERT INTO comment VALUES (8005, 'http://www.iamhans.com', 8001, 8000, NULL, '2016-04-04 08:03:12');
--Vote
INSERT INTO vote VALUES (8000, -1, 8006, NULL, 8002);
INSERT INTO vote VALUES (8001, 1, NULL, 8003, 8002);
INSERT INTO vote VALUES (8019, -1, 8017, NULL, 8002);
INSERT INTO vote VALUES (8020, -1, 8017, NULL, 8003);
INSERT INTO vote VALUES (8021, -1, 8017, NULL, 8004);
INSERT INTO vote VALUES (8022, -1, 8017, NULL, 8005);
INSERT INTO vote VALUES (8023, -1, 8017, NULL, 8006);
INSERT INTO vote VALUES (8024, -1, 8017, NULL, 8007);
INSERT INTO vote VALUES (8025, 1, NULL, 8007, 8000);
INSERT INTO vote VALUES (8026, 1, NULL, 8007, 8004);
INSERT INTO vote VALUES (8027, 1, NULL, 8007, 8009);
INSERT INTO vote VALUES (8006, -1, NULL, 8012, 8000);
INSERT INTO vote VALUES (8007, -1, NULL, 8012, 8002);
INSERT INTO vote VALUES (8008, -1, NULL, 8012, 8003);
INSERT INTO vote VALUES (8009, -1, NULL, 8012, 8004);
INSERT INTO vote VALUES (8010, -1, NULL, 8012, 8005);
INSERT INTO vote VALUES (8011, -1, NULL, 8012, 8006);
INSERT INTO vote VALUES (8012, -1, NULL, 8012, 8007);
INSERT INTO vote VALUES (8013, -1, NULL, 8012, 8008);
INSERT INTO vote VALUES (8014, -1, NULL, 8012, 8009);
INSERT INTO vote VALUES (8015, -1, NULL, 8012, 8012);
INSERT INTO vote VALUES (8002, 1, NULL, 8005, 8003);
INSERT INTO vote VALUES (8004, 1, NULL, 8005, 8000);
INSERT INTO vote VALUES (8003, 1, NULL, 8005, 8004);
INSERT INTO vote VALUES (8005, 1, NULL, 8005, 8005);
INSERT INTO vote VALUES (8016, 1, NULL, 8012, 8001);
INSERT INTO vote VALUES (8017, -1, NULL, 8012, 8011);
INSERT INTO vote VALUES (8018, -1, 8017, NULL, 8000);
---Report
INSERT INTO report VALUES (8000, 'Spam', '2016-04-11 08:23:42', 8000, NULL, 8012, NULL);
INSERT INTO report VALUES (8001, 'Spam', '2016-04-11 09:13:09', 8000, 8012, NULL, NULL);
INSERT INTO report VALUES (8002, 'Spam', '2016-04-11 10:53:02', 8000, NULL, NULL, 8005);


