/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zeppelin.lucene;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.interpreter.InterpreterResult.Code;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;
import com.zeppelin.hackathon.client.ClientException;
import com.zeppelin.hackathon.client.DataClients;
import com.zeppelin.hackathon.gen.Article;
import com.zeppelin.hackathon.gen.ArticleRequest;
import com.zeppelin.hackathon.gen.ArticleResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * org.apache.zeppelin.lucene.LuceneInterpreter interpreter for Zeppelin.
 *
 * @author Leemoonsoo
 * @author anthonycorbacho
 *
 */
public class LuceneInterpreter extends Interpreter {
  Logger logger = LoggerFactory.getLogger(LuceneInterpreter.class);
  DataClients dataClients = null;
  int queryTimeOut = 3000;

  static {
    Interpreter.register("lucene", LuceneInterpreter.class.getName());
  }

  public LuceneInterpreter(Properties property) {
    super(property);
  }

  @Override
  public void open() {
    dataClients = new DataClients("172.16.5.239:5678,172.16.5.239:5678");
  }

  @Override
  public void close() {}


  @Override
  public InterpreterResult interpret(String query, InterpreterContext contextInterpreter) {
    logger.info("Run lucene query '" + query + "'");
    ArticleRequest req = new ArticleRequest();
    req.setKeyword(query);
    req.setOffset(0);
    try {
      ArticleResponse response = dataClients.searchArticle(req);
      List<Article> articles = response.getArticles();
      String message = "";
      for (Article article : articles) {
        message += article.getTitle();
        message += "\t";
        message += article.getUrl();
        message += "\t";
        message += article.getContent();
        message += "\n";
      }
      return new InterpreterResult(InterpreterResult.Code.SUCCESS, message);
    } catch (ClientException e) {
      logger.error("Can not query " + query, e);
      return new InterpreterResult(Code.ERROR, e.getMessage());
    }
  }

  @Override
  public void cancel(InterpreterContext context) {}

  @Override
  public FormType getFormType() {
    return FormType.SIMPLE;
  }

  @Override
  public int getProgress(InterpreterContext context) {
    return 0;
  }

  @Override
  public Scheduler getScheduler() {
    return SchedulerFactory.singleton().createOrGetFIFOScheduler(
            LuceneInterpreter.class.getName() + this.hashCode());
  }

  @Override
  public List<String> completion(String buf, int cursor) {
    return null;
  }

}
