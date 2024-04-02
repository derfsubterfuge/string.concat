#!/usr/bin/env bash
mvn clean install && chmod u+x target/string.concat-*.jar && java -jar target/string.concat-*.jar
