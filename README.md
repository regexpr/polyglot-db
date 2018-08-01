# PolyG-DBP - [PolyG]lot - [D]ata[B]ase[P]erformance
## Big Data Traineeship SuSe 2018

See https://dbs.uni-leipzig.de/study/ss_2018/bigdprak

## Contributor
* Hyeon Ung Kim (LastMinuteHero)
* Tim Niehoff (regexpr)
## Teacher
* J. Zschache

# Requirements
* Maven
* MongoDB
* Neo4J
* neo4j doc manager

Please visit our Wiki to find out which Versions of the requiremental software we have used:
https://github.com/regexpr/polyglot-db/wiki/Helpful-Links

# Installing
<pre>
git clone https://github.com/regexpr/polyglot-db
cd polyglot-db/PolyG-DBP/PolyG-DBP/
mvn install
</pre>

# Run
1. Run one Neo4j database in the background:
<pre>
neo4j/bin/neo4j console
</pre>

2.a Run one mongod replica set in the background
<pre>
mongod --replSet exSet --config ~/bigdata/mongo/mongodb.conf
</pre>
2.b Initialize the replica set
<pre>
mongo
rs.initiate()
</pre>

3. Run PolyG-DBP
<pre>
cd polyglot-db/PolyG-DBP/PolyG-DBP/target
java -jar PolyG-DBP-0.1.jar
</pre>

# Usage
* java -jar PolyG-DBP-0.1.jar help:
            Displays this help;
* java -jar PolyG-DBP-0.1.jar list:
            lists all queries provided by PolyG-DBP.;
* java -jar PolyG-DBP-0.1.jar [Options] QUERY
            Benchmark with the given query. Example
            <pre>java -jar PolyG-DBP-0.1.jar q1</pre>
# Options (can be given in any order)
* -i, --inputPath to the directory with JSON file(s). Example: 
<pre>java -jar PolyG-DBP-0.1.jar q1 -i yelp</pre>
* -nb, --neo4jAddressBolt1Adress of the neo4j instance with the bolt address. Example: 
<pre>java -jar PolyG-DBP-0.1.jar q1 -nb localhost:7687</pre>
* -nr, --neo4jAddressRemoteAdress of the neo4j instance with the remote address. Example: 
<pre>java -jar PolyG-DBP-0.1.jar q1 -nr localhost:7474</pre>
* -m, --mongoAddressAdress of the mongodb instance. Example: 
<pre>java -jar PolyG-DBP-0.1.jar q1 -m localhost:27017</pre>
* -md, --mongoDatabaseName of the mongodb database. Example: 
<pre>java -jar PolyG-DBP-0.1.jar q1 -md yelp</pre>
* -r, --reduceImport just a certain amount of lines of each input JSON. Example: 
<pre>java -jar PolyG-DBP-0.1.jar q1 -r 300</pre>
    
 # Available Queries for Yelp Dataset for both Data bases:
 * q1: Output me all business names and ids a <specific user> rated with minumum of <stars>
 * q2: Output the average stars of all businesses
