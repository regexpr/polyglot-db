# Konzeptioneller Entwurf

Dieses Entwurfsdokument beschreibt konzeptionell den Ablauf, sowie die Architektur unter Einbeziehung der genutzten Frameworks von PolyDBTesting.

## Allgemeines Vorhaben
Wir widmen uns mit unserem Projekt dem Vergleich einer Graphdatenbank mit einer Dokumentendatenbank.

Unsere Software PolyDBTesting soll hierfür mithilfe von Benchmarking in der Lage sein, Aufschlüsse darüber geben zu können, wie sich die Struktur der Datenbank auf die Performanz auswirkt. Zu diesem Zweck soll die Dauer für das Ausführen gleicher Anfragen für den gleichen Datensatz auf ihnen verglichen werden.

Darüber hinaus soll ebenfalls das folgende Szenario betrachtet werden: Sofern eine Graphdatenbank gegenüber der Dokumentendatenbank bei bestimmten Anfragetypen deutlich performanter ist, lohnt sich die Bereitstellung einer Polyglot Anwendung, d.h. die Verwendung unterschiedlicher Datenbanksysteme trotz der damit notwendigen Synchronisation? Hierfür soll  PolyDBTesting ebenfalls Messmethoden bereitstellen. @TODO: Kann man Synchronisation überhaupt simulieren? Was ist, wenn es im Hintergrund geschieht?


## Ablauf und Architektur

Die Software PolygDBTesting wird in Java als Konsolenapplikation umgesetzt.
Konkret werden **Neo4j** als Graphdatenbank und **MongoDB** als Dokumentendatenbank untersucht. Für ihre Synchronisation werden **mongo-connector** und **neo4j doc manager** verwendet.
Zum Testen der Anfragen benutzt PolyDBTesting den **Yelp Open Datensatz** (https://www.yelp.com/dataset).

![Architektur](architecture.png "Architektur")
@TODO: Framework wegstreichen in der Abbildung.

Der Anwender kann beim Aufruf der Applikation mithilfe von Befehlszeilenparametern zwischen verschiedenen, vorgefertigten Anfragen an die Datenbanken (DB) wählen. Diese passen zum Yelp Datensatz und sind sowohl in der Mongo und Neo4j kompatiblen Anfragesprache vorgeschrieben. Desweiteren kann der Anwender wählen, ob der Datensatz in den DB bereits existiert, oder noch über PolyDBTsting vollständig/teilweise in die DB geladen werden sollen. Das optionale Kürzen der Datenmengen erfolgt über die Klasse *DataReducer* (siehe Abbildung "Architektur").

PolyDBTesting kümmert sich nach dem Aufruf um das Starten der DB und ggf. das Laden der Datensätze zunächst in die MongoDB mithilfe von mongoimport via *DatebaseServices*. Anschließend werden mongoconnector in Verbindung mit neo4j doc manager aufgerufen, um die Daten von MongoDB in die Neo4J DB zu laden. 

Die Anfragen werden nun in der Klasse *QueryHandler*  mithilfe der Bibliotheken **Mongo-Java-Driver** und **Neo4j Java Driver** an die Datenbanken seriell weitergeleitet. Die Klasse *Benchmark* stoppt für das Ausführen der Anfragen die Zeit.
Als Output erhält der Anwender ein Log und eine Konsolenausgabe mit den Ergebnissen.

@TODO: Simulation der Synchronisation

## Optionale Ziele
* Verwendung von anderen Datensätzen
* Benutzung von Apoc zum Einladen des Datensatzes für Neo4j
