SET client_encoding = 'UTF8';
--Track
INSERT INTO tag VALUES (8000, 'An1I', 'Analysis 1 für Informatiker', true);
INSERT INTO tag VALUES (8001, 'An2I', 'Analysis 2 für Informatiker', true);
INSERT INTO tag VALUES (8002, 'AD1', 'Algorithmen und Datenstrukturen 1', true);
INSERT INTO tag VALUES (8003, 'WED1', 'Web Engineering + Design 1', true);
INSERT INTO tag VALUES (8004, 'AD2', 'Algorithmen und Datenstrukturen 2', true);
INSERT INTO tag VALUES (8005, 'Tree', NULL, false);
INSERT INTO tag VALUES (8006, 'Pattern', NULL, false);
INSERT INTO tag VALUES (8007, 'SE1', 'Software Engineering 1', true);
INSERT INTO tag VALUES (8008, 'SE2', 'Software Engineering 2', true);
INSERT INTO tag VALUES (8009, 'Testing', NULL, false);
INSERT INTO tag VALUES (8010, 'Einfache Operationen', NULL, false);
INSERT INTO tag VALUES (8011, 'DMI', 'Diskrete Mathematik für Informatiker', true);
INSERT INTO tag VALUES (8012, 'Logik', NULL, false);
INSERT INTO tag VALUES (8013, 'Mathematik', NULL, false);
INSERT INTO tag VALUES (8014, 'Software Engineering', NULL, false);
INSERT INTO tag VALUES (8015, 'VSS', 'Verteilte SW-Systeme', true);
INSERT INTO tag VALUES (8016, 'APF', 'Advanced Pattern Frameworks', true);
INSERT INTO tag VALUES (8017, 'Integral', NULL, false);
INSERT INTO tag VALUES (8018, 'Ableiten', NULL, false);
INSERT INTO tag VALUES (8019, 'Clean Code', NULL, false);
INSERT INTO tag VALUES (8020, 'Error Handling', NULL, false);
INSERT INTO tag VALUES (8021, 'Funktion', NULL, false);
INSERT INTO tag VALUES (8022, 'Allgemein', NULL, true);
--
INSERT INTO exoluser VALUES (8000, 'Franz', 'franz@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8003, 'Simon', 'simon@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8005, 'Denarya', 'denarya@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8006, 'Silwyna', 'silwyna@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8007, 'Angela', 'angela@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8008, 'Tony', 'tony@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8009, 'Sandro', 'sandro@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8010, 'Mario', 'mario@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8012, 'Lorem Ipsum', 'lorem.ipsum@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8011, 'Marco', 'marco@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8001, 'Hans', 'hans@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8004, 'p', 'katherina@hsr.ch', '', false);
INSERT INTO exoluser VALUES (8002, 'Blubberduck', 'blubberduck@hsr.ch', '', true);
--Exercise
INSERT INTO exercise VALUES (8004, '<p>Was ist der Nachteil von Quicksort?</p>', '2016-02-17 12:03:12', NULL, 'Quicksort', 8003);
INSERT INTO exercise VALUES (8003, '<p>Welche Sortierungsmethode ist die Beste?</p>', '2016-02-17 05:52:16', NULL, 'Sortieren', 8003);
INSERT INTO exercise VALUES (8001, '<p>Was ist das Gegenteil von false?</p>', '2016-02-17 03:04:05', NULL, 'Basic Logic', 8000);
INSERT INTO exercise VALUES (8007, '<p>Was ist die erste Ableitung von y=5x?</p>

<p>y''=???</p>', '2015-12-02 08:00:02', NULL, 'Ableitung 1a)', 8002);
INSERT INTO exercise VALUES (8011, '<p>Wie gross ist die Fl&auml;che der Funktion y=x^3 zwischen dem Punkt x1=-1 und x2=1?', '2016-01-14 16:03:23', NULL, 'Fläche einer Funktion', 8002);
INSERT INTO exercise VALUES (8012, '<p>Besuche meine super Website: http://www.iamhans.com', '2016-04-11 21:45:33', NULL, 'XXX', 8001);
INSERT INTO exercise VALUES (8008, '<p>Was bedeutet TDD genau?</p>', '2016-04-01 09:24:24', NULL, 'TDD Definition', 8008);
INSERT INTO exercise VALUES (8009, '<p>Was ist ein Unit Test?</p>', '2016-04-01 09:27:12', NULL, 'Unit Testing', 8008);
INSERT INTO exercise VALUES (8010, '<p>Was ist der Unterschied zwischen einem System Test und einem Unit Test?</p>', '2016-04-02 14:07:12', NULL, 'System Test vs. Unit Test', 8005);
INSERT INTO exercise VALUES (8005, '<p>In einem Code gibt es diverse Stellen mit &bdquo;Catch-All&ldquo; von Exceptions. Die Absicht dahinter ist es, die Software fehlertolerant zu halten.</p>

<p>DataSet load() {<br />
&nbsp; try {<br />
&nbsp; &nbsp; // read logic<br />
&nbsp; } catch (Exception e) {<br />
&nbsp;&nbsp; &nbsp;return null;<br />
&nbsp; }<br />
}</p>

<p>DataSet filter(DataSet d) {<br />
&nbsp; try {<br />
&nbsp;&nbsp;&nbsp; // filter logic<br />
&nbsp; } catch (Exception e) {<br />
&nbsp;&nbsp;&nbsp; return null;<br />
&nbsp; }<br />
}</p>

<p>void save(DataSet d) {<br />
&nbsp; try {<br />
&nbsp;&nbsp;&nbsp; // save to file<br />
&nbsp; } catch (Exception e) { }<br />
}</p>

<p>void controlLoop() {<br />
&nbsp; while(&hellip;) {<br />
&nbsp;&nbsp;&nbsp; // wait for condition save(filter(load());<br />
&nbsp; }<br />
}</p>

<p>a) Welche Probleme sehen Sie bei diesem Exception Handling Design?</p>

<p>b) Welche Design-&Auml;nderungen empfehlen Sie?</p>', '2016-03-18 14:52:03', NULL, 'Error Handling Design', 8008);
INSERT INTO exercise VALUES (8006, '<p>Entwerfen Sie ein Error-Handling Policy f&uuml;r Ihr Engineering-Projekt. Legen Sie dabei fest, wie Sie Exceptions, Assertions und Logging verwenden wollen.</p>

<p>Falls Sie kein Engineering-Projekt haben, verwenden Sie das kleine Java Beispiel-Projekt ImageGallery aus der Vorlage.</p>', '2016-03-18 15:02:14', NULL, 'Erro Handling Policy', 8008);
INSERT INTO exercise VALUES (8002, '<p>Wie teuer ist eine Suchoperation eines AVL-Trees mit n Elementen?</p>

<p>L&ouml;sung in O-Notation angeben.</p>', '2016-02-17 23:55:34', NULL, 'AVL-Tree Suchkosten', 8002);

INSERT INTO exercise VALUES (8000, '<p>Was gibt 1+2?</p>', '2015-01-08 00:00:00', NULL, 'Grundlegende Mathematik', 8000);
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
INSERT INTO solution VALUES (8013, '<p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.</p>', '2016-04-03 00:00:00', NULL, false, 8005, 8012);
INSERT INTO solution VALUES (8014, '<p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.</p>', '2016-04-03 00:00:00', NULL, false, 8007, 8012);
INSERT INTO solution VALUES (8001, '<p>Es ist 3.</p>', '2016-03-05 23:53:23', NULL, true, 8000, 8000);
INSERT INTO solution VALUES (8002, '<p>Ich denke es ist true.</p>', '2016-03-27 15:07:47', NULL, false, 8001, 8001);
INSERT INTO solution VALUES (8003, '<p>Es ist true...</p>', '2016-04-02 07:44:10', NULL, false, 8001, 8000);
INSERT INTO solution VALUES (8004, '<p>H&auml;ngt sehr von der Definiton von + ab. Standartm&auml;ssig ist es 3.</p>', '2015-02-22 22:22:22', NULL, false, 8000, 8002);
INSERT INTO solution VALUES (8005, '<p>log(n)</p>', '2015-12-31 23:59:59', NULL, true, 8002, 8002);
INSERT INTO solution VALUES (8015, '<p>y''=5</p>', '2015-12-04 00:00:00', NULL, true, 8007, 8002);
INSERT INTO solution VALUES (8016, '<p>5</p>', '2015-12-06 08:07:56', NULL, false, 8007, 8005);
INSERT INTO solution VALUES (8017, '<p>http://www.iamhans.com</p>', '2015-04-11 21:45:56', NULL, true, 8012, 8001);
INSERT INTO solution VALUES (8007, '<p>a) Fehler bleiben unbemerkt, insbesondere auch Programmierfehler. Daher sind Folgefehler immanent: Bei Fehler in load() und filter() wird trotzdem save() ausgef&uuml;hrt, was beispielsweise zu Schreiben von leere Daten f&uuml;hren kann. Zudem wird das Testen der Methoden schwieriger und die Wiederverwendbarkeit eingeschr&auml;nkt, weil im Fehlerfall keine Exception an den Aufrufer propagiert wird.</p>

<p>b) Empfohlene &Auml;nderungen:</p>

<ul>
	<li>Fehler in Hilfsmethoden (load, filter, save) an Aufrufer propagieren.</li>
	<li>Abfangen von spezifischen Exception-Typen, die von externen Fehler stammen (z.B. IOExceptions).</li>
	<li>Bei Fehler in Teilmethode (z.B. in load()), weitere Aufrufe (z.B. save()) nicht ausf&uuml;hren.</li>
	<li>Loggen der aufgetretenen Fehler.</li>
</ul>', '2016-03-18 14:59:11', NULL, true, 8005, 8008);
INSERT INTO solution VALUES (8009, 'a) Fehler bleiben unbemerkt was problematisch ist.

b) Kein Catch-All benutzen', '2016-03-18 19:35:48', NULL, false, 8005, 8002);
INSERT INTO solution VALUES (8010, '<p>a) Kritische Fehler werden sozusagen einfach ignoriert.</p>

<p>b) Jeden Fehler einzeln abfangen (nach Typ) und jenachdem anders behandeln.</p>', '2016-03-18 17:23:21', NULL, false, 8005, 8010);
INSERT INTO solution VALUES (8011, '<p>a) Ich sehe dabei nicht wirklich ein Problem.</p>

<p>b) Ich w&uuml;rde gar keine &Auml;nderung hier empfehlen.</p>', '2016-03-20 09:04:29', NULL, false, 8005, 8007);
INSERT INTO solution VALUES (8008, '<p>a) kA</p>

<p>b) kA,&nbsp;Nur f&uuml;r die L&ouml;sung hier</p>', '2016-03-19 05:59:23', NULL, false, 8005, 8011);
INSERT INTO solution VALUES (8006, '<p>50CHF?</p>', '2016-01-01 00:00:00', NULL, true, 8002, 8001);
INSERT INTO solution VALUES (8000, '<p>Es ist nat&uuml;rlich 4</p>', '2016-03-05 19:55:23', NULL, false, 8000, 8001);
INSERT INTO solution VALUES (8012, '<p><a href="http://www.iamhans.com">http://www.iamhans.com</a></p>', '2016-04-08 04:02:58', NULL, false, 8005, 8001);
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
INSERT INTO comment VALUES (8000, 'Bist du dir da wirklich sicher das dies stimmt?', 8001, 8001, NULL, '2016-04-01 08:23:42');
INSERT INTO comment VALUES (8001, 'HOW CAN YOU BE SO WRONG!', 8000, 8000, NULL, '2016-03-27 14:52:19');
INSERT INTO comment VALUES (8002, 'Was heisst O-Notation? noch nie gehört davon..', 8001, NULL, 8002, '2016-04-07 17:55:12');
INSERT INTO comment VALUES (8003, 'Vielleicht wäre (was sind die Kosten) besser gewesen wie (Wie teuer ist)', 8004, NULL, 8002, '2016-04-07 18:12:55');
INSERT INTO comment VALUES (8004, 'Du hast auch keine Ahnung von O-Notation?', 8004, 8006, NULL, '2016-04-06 11:12:55');
INSERT INTO comment VALUES (8005, 'http://www.iamhans.com', 8001, 8000, NULL, '2016-04-04 08:03:12');
--Vote
INSERT INTO vote VALUES (8000, 1, 8002, NULL, 8000);
INSERT INTO vote VALUES (8001, -1, 8000, NULL, 8000);
INSERT INTO vote VALUES (8002, 1, 8004, NULL, 8000);
INSERT INTO vote VALUES (8003, -1, NULL, 8012, 8000);
INSERT INTO vote VALUES (8004, -1, NULL, 8012, 8003);
INSERT INTO vote VALUES (8005, -1, NULL, 8001, 8003);
INSERT INTO vote VALUES (8006, -1, NULL, 8000, 8003);
INSERT INTO vote VALUES (8007, 1, NULL, 8005, 8003);
INSERT INTO vote VALUES (8008, 1, 8015, NULL, 8005);
INSERT INTO vote VALUES (8009, 1, NULL, 8001, 8006);
INSERT INTO vote VALUES (8010, -1, NULL, 8012, 8006);
INSERT INTO vote VALUES (8011, 1, 8007, NULL, 8007);
INSERT INTO vote VALUES (8012, -1, 8012, NULL, 8007);
INSERT INTO vote VALUES (8013, -1, 8008, NULL, 8007);
INSERT INTO vote VALUES (8014, 1, 8010, NULL, 8007);
INSERT INTO vote VALUES (8015, 1, 8009, NULL, 8007);
INSERT INTO vote VALUES (8016, 1, NULL, 8005, 8007);
INSERT INTO vote VALUES (8017, 1, 8010, NULL, 8008);
INSERT INTO vote VALUES (8018, -1, 8012, NULL, 8008);
INSERT INTO vote VALUES (8019, -1, 8011, NULL, 8008);
INSERT INTO vote VALUES (8020, -1, 8008, NULL, 8008);
INSERT INTO vote VALUES (8021, 1, NULL, 8002, 8008);
INSERT INTO vote VALUES (8022, 1, NULL, 8006, 8008);
INSERT INTO vote VALUES (8023, 1, NULL, 8010, 8008);
INSERT INTO vote VALUES (8024, -1, NULL, 8012, 8008);
INSERT INTO vote VALUES (8025, 1, NULL, 8011, 8009);
INSERT INTO vote VALUES (8026, 1, NULL, 8005, 8009);
INSERT INTO vote VALUES (8027, 1, NULL, 8006, 8009);
INSERT INTO vote VALUES (8028, -1, NULL, 8012, 8009);
INSERT INTO vote VALUES (8029, 1, 8007, NULL, 8010);
INSERT INTO vote VALUES (8030, -1, 8012, NULL, 8010);
INSERT INTO vote VALUES (8031, 1, NULL, 8009, 8010);
INSERT INTO vote VALUES (8032, 1, 8007, NULL, 8012);
INSERT INTO vote VALUES (8033, 1, NULL, 8005, 8012);
INSERT INTO vote VALUES (8034, -1, 8012, NULL, 8012);
INSERT INTO vote VALUES (8035, -1, NULL, 8012, 8012);
INSERT INTO vote VALUES (8036, 1, 8007, NULL, 8011);
INSERT INTO vote VALUES (8037, 1, NULL, 8005, 8011);
INSERT INTO vote VALUES (8038, 1, 8011, NULL, 8011);
INSERT INTO vote VALUES (8039, 1, NULL, 8012, 8011);
INSERT INTO vote VALUES (8040, 1, NULL, 8001, 8011);
INSERT INTO vote VALUES (8041, 1, 8001, NULL, 8001);
INSERT INTO vote VALUES (8042, 1, NULL, 8000, 8001);
INSERT INTO vote VALUES (8043, 1, 8005, NULL, 8001);
INSERT INTO vote VALUES (8044, -1, 8003, NULL, 8001);
INSERT INTO vote VALUES (8045, -1, NULL, 8001, 8001);
INSERT INTO vote VALUES (8046, -1, 8008, NULL, 8001);
INSERT INTO vote VALUES (8047, 1, NULL, 8002, 8004);
INSERT INTO vote VALUES (8048, -1, 8006, NULL, 8004);
INSERT INTO vote VALUES (8049, 1, 8005, NULL, 8004);
INSERT INTO vote VALUES (8050, -1, 8006, NULL, 8002);
INSERT INTO vote VALUES (8051, -1, 8000, NULL, 8002);
INSERT INTO vote VALUES (8052, -1, NULL, 8000, 8002);
INSERT INTO vote VALUES (8053, 1, 8016, NULL, 8002);
INSERT INTO vote VALUES (8054, 1, NULL, 8005, 8002);
INSERT INTO vote VALUES (8055, 1, 8007, NULL, 8002);
INSERT INTO vote VALUES (8056, 1, 8010, NULL, 8002);
INSERT INTO vote VALUES (8057, -1, 8012, NULL, 8002);
INSERT INTO vote VALUES (8058, -1, 8008, NULL, 8002);
INSERT INTO vote VALUES (8059, -1, 8011, NULL, 8002);
INSERT INTO vote VALUES (8060, 1, NULL, 8004, 8002);
INSERT INTO vote VALUES (8061, 1, NULL, 8010, 8002);
INSERT INTO vote VALUES (8062, 1, NULL, 8003, 8002);
---Report
INSERT INTO report VALUES (8000, 'Spam', '2016-04-11 08:23:42', 8000, NULL, 8012, NULL);
INSERT INTO report VALUES (8001, 'Spam', '2016-04-11 09:13:09', 8000, 8012, NULL, NULL);
INSERT INTO report VALUES (8002, 'Spam', '2016-04-11 10:53:02', 8000, NULL, NULL, 8005);


