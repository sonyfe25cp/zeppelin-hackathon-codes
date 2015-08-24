#!/bin/sh

ps -ef|grep WeixinPostSearcher|grep -v grep|cut -c 9-15|xargs kill -9

echo 'old thread is killed'

nohup /opt/jdk1.8.0_05/bin/java -cp risk-server-0.1-SNAPSHOT-jar-with-dependencies.jar com.zeppelin.hackathon.engine.app.WeixinPostSearcher -port 5680 -p weixinPost-index > post.search.log &
echo 'post searcher is running'



