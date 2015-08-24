#!/bin/sh

cd /home/work/project/risk-server

/opt/jdk1.8.0_05/bin/java -cp risk-server-0.1-SNAPSHOT-jar-with-dependencies.jar com.zeppelin.hackathon.engine.app.NewsIndexer -p news-index-new # > news-index.log

echo 'news index over'
rm -rf news-index
echo 'delete old news-index'
mv news-index-new news-index
echo 'new index is already'



