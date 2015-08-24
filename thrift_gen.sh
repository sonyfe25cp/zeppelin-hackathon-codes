#! /bin/bash

rm -rf hackathon-client/gen-java
rm -rf hackathon-server/gen-java
#rm -rf risk-parser/gen-java

DIR=`pwd`
FILE="${DIR}/data.thrift"

(cd hackathon-client && thrift -gen java ${FILE})
(cd hackathon-server&& thrift -gen java ${FILE})
#(cd risk-parser&& thrift -gen java ${FILE})

(cd python-scripts && python gen_hooks.py --file ../data.thrift --mode gen-java > ../hackathon-client/src/main/java/com/zeppelin/hackathon/client/DataClients.java)



if [[ $# -ne 0 ]]; then
    (cd thrift-router && ./gen.sh)
fi
