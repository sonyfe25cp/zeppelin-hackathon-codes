#!/bin/sh

cd /home/work/project/risk-server

/opt/jdk1.8.0_05/bin/java -cp risk-server-0.1-SNAPSHOT-jar-with-dependencies.jar com.zeppelin.hackathon.engine.app.WeixinAccountIndexer -p weixinAccount-index-new > weixinAccount-index.log

echo 'weixinAccount index over'
rm -rf weixinAccount-index
echo 'delete old news-index'
mv weixinAccount-index-new weixinAccount-index
echo 'new index is already'



