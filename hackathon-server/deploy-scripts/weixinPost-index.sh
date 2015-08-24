#!/bin/sh

cd /home/work/project/risk-server

/opt/jdk1.8.0_05/bin/java -cp risk-server-0.1-SNAPSHOT-jar-with-dependencies.jar com.zeppelin.hackathon.engine.app.WeixinPostIndexer -p weixinPost-index-new > weixinPost-index.log

echo 'weixinPost index over'
rm -rf weixinPost-index
echo 'delete old news-index'
mv weixinPost-index-new weixinPost-index
echo 'new index is already'



