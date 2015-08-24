##Zeppelin on Lucene

Our project in the Hackathon supported by Spark and Zeppelin Community.

Location: The No.1 building of Microsoft, Beijing

Time: 2015-8-22 to 2015-8-23

Hackathon: [https://hackdash.org/dashboards/bj0815](https://hackdash.org/dashboards/bj0815)

Event Schedule: [http://beijingsparkcommunity.azurewebsites.net/](http://beijingsparkcommunity.azurewebsites.net/)


##How to use this interpreter?

1. Move the `lucene` folder to the source code of Zeppelin.
2. Add `org.apache.zeppelin.lucene.LuceneInterpreter` to `conf/zeppelin-site.xml`.
    
    <property>
        <name>zeppelin.interpreters</name>
        <value>org.apache.zeppelin.spark.SparkInterpreter,org.apache.zeppelin.spark.PySparkInterpreter,org.apache.zeppelin.spark.SparkSqlInterpreter,org.apache.zeppelin.spark.DepInterpreter,org.apache.zeppelin.markdown.Markdown,org.apache.zeppelin.angular.AngularInterpreter,org.apache.zeppelin.shell.ShellInterpreter,org.apache.zeppelin.hive.HiveInterpreter,org.apache.zeppelin.tajo.TajoInterpreter,org.apache.zeppelin.flink.FlinkInterpreter,org.apache.zeppelin.lucene.LuceneInterpreter</value>
        <description>Comma separated interpreter configurations. First interpreter become a default</description>
    </property>
3. Compile the source code with `mvn clean compile`.
4. Run the Zeppelin server.


##TO DO

Things need to be done are list as follows:

1. How to insert this interpreter into Zeppelin.
2. A better solution to bridge Zeppelin and Lucene.

##Group Members

* Chen Jie
* Wang Chen
* Hu Cong
* Song Ming
