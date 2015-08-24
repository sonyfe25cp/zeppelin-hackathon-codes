package com.zeppelin.hackathon.engine.service;

import com.zeppelin.hackathon.gen.Article;
import com.zeppelin.hackathon.gen.ArticleRequest;
import com.zeppelin.hackathon.gen.ArticleResponse;
import com.zeppelin.hackathon.gen.DataService;
import org.apache.thrift.TException;

public class ADataService extends AIndexSearcher implements DataService.Iface {

    @Override
    public Article findArticleById(long id) throws TException {
        throw new TException();
    }

    @Override
    public ArticleResponse searchArticle(ArticleRequest req) throws TException {
        throw new TException();
    }

}
