# Konzeptioneller Entwurf

Aufgabenstellung: Konzeptioneller Entwurf Es ist ein Entwurfsdokument anzufertigen, welches konzeptionell den Ablauf und die Architektur ihrer Anwendung darstellt. Diesbezüglich sollen Sie beschreiben wie Sie die jeweiligen Frameworks nutzen. Das Dokument soll sich vom Umfang auf 2-4 Seiten beschränken.

## Vorhaben
Our Project is to create a benchmarking software for NoSQL databases, that  measures the time it takes to absolve a transaction. The databases we will use are MongoDB and Neo4j.

## Ablauf

We will be using the Yelp dataset to create our databases.  At first we will use Mongo-import to load the Yelp dataset into MongoDB. Since MongoDB uses JSON we can just take the dataset as it is given by yelp and load it into the database. 

Neo4j on the other hand is a graph-based database, thus a different structure is required to build our dataset. We will be using two different types of methods to import our dataset.

First we will be using Mongo-connector to synchronize our databases. Mongo-connector sets up a listener in the MongoDB, which translates every change in the MongoDB to a cypher query and loads in the translated dataset into Neo4j. This takes a great load off the work needed to import the dataset into Neo4j and solves the synchronization issue of the two databases.

Yet this method is quite static, which means that the structure of the resulting Neo4j database is given by the Mongo-connector.

To measure how much the structure of the database influences the performance, we will be using Apoc, a Cypher library used for importing JSON data. Using this tool, we will create a custom data structure optimized for the queries we will be executing.

Our software will be written in Java and we will be using given java libraries for our program to communicate with our databases. The program will connect to our MongoDB and execute the query. When executed we will cache the starting-time and as soon as the transaction is completed we will take the end-time and measure the time it took for the transaction to complete.

## Architektur

![Architektur](architecture.png "Architektur")
