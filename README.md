# PolyG-DBP - [PolyG]lot - [D]ata[B]ase[P]erformance
## Big Data Traineeship SuSe 2018

See https://dbs.uni-leipzig.de/study/ss_2018/bigdprak

## Contributor
* Hyeon Ung Kim (LastMinuteHero)
* Tim Niehoff (regexpr)

# Requirements
* MongoDB
* Neo4J
* neo4j doc manager

# Installing
<pre>
git clone https://github.com/regexpr/polyglot-db
</pre

# Usage
1. Run one Neo4j database in the background:
<pre>
mongod --replSet Rocket --config ~/bigdata/mongo/mongodb.conf                 
</pre>
<pre>
</pre>
2.a Run one mongod replica set in the background
<pre>
mongod --replSet timRocket --config ~/bigdata/mongo/mongodb.conf
</pre>
2.b Initialize the replica set
<pre>
mongo
rs.initiate()
</pre>
