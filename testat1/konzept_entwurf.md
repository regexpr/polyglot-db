# Konzeptioneller Entwurf

Dieses Entwurfsdokument beschreibt konzeptionell den Ablauf, sowie die Architektur unter Einbeziehung der genutzten Frameworks von PolyDBTesting.

## allgemeines Vorhaben
Wir widmen uns mit unserem Projekt dem Vergleich einer Graphdatenbank mit einer Dokumentendatenbank.
Unsere Software PolyDBTesting soll hierfür mithilfe von Benchmarking in der Lage sein, Aufschlüsse darüber geben zu können, wie sich die Struktur der Datenbank auf die Performanz auswirkt. Zu diesem Zweck soll die Dauer für das Ausführen gleicher Anfragen für den gleichen Datensatz auf ihnen verglichen werden.
Darüber hinaus soll ebenfalls das folgende Szenario betrachtet werden: Sofern eine Graphdatenbank gegenüber der Dokumentendatenbank bei bestimmten Anfragetypen deutlich performanter ist, lohnt sich die Bereitstellung einer Polyglot Anwendung, d.h. die Verwendung unterschiedlicher Datenbanksysteme trotz der damit notwendigen Synchronisation? Hierfür soll  PolyDBTesting ebenfalls Messmethoden bereitstellen.


## Ablauf und Architektur

Die Software PolygDBTesting wird in Java als Konsolenapplikation umgesetzt.
Konkret werden **Neo4j** als Graphdatenbank und **MongoDB** als Dokumentendatenbank untersucht. Für ihre Synchronisation werden **mongo-connector** und **neo4j doc manager** verwendet.
Zum Testen der Anfragen benutzt PolyDBTesting den **Yelp Open Datensatz** (https://www.yelp.com/dataset).
@TODO:
Java Bibliotheken: Neo4j API, Mongo API, Benchmarking, Processes



![Architektur](architecture.png "Architektur")
@TODO: Framework wegstreichen in der Abbildung.

## Optionale Ziele
* Verwendung von anderen Datensätzen
* Benutzung von Apoc zum Einladen des Datensatzes für Neo4j.
