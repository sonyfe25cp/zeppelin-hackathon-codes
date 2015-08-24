#!/bin/sh

ps -ef|grep NewsSearcher|grep -v grep|cut -c 9-15|xargs kill -9
echo 'old thread is killed'


nohup /opt/jdk1.8.0_05/bin/java -cp risk-server-0.1-SNAPSHOT-jar-with-dependencies.jar com.zeppelin.hackathon.engine.app.NewsSearcher -port 5678 > news.search.log &
echo 'news searcher is running'



