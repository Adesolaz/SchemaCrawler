@echo off
java -classpath ../_schemacrawler/lib/*;lib/* schemacrawler.tools.hsqldb.Main -database=schemacrawler -user=sa -password= -infolevel=standard -command=tables.select %*