package com.zeppelin.hackathon.engine.app;

import com.mysql.jdbc.StringUtils;
import com.zeppelin.hackathon.engine.service.ADataService;
import com.zeppelin.hackathon.gen.Article;
import com.zeppelin.hackathon.gen.ArticleRequest;
import com.zeppelin.hackathon.gen.ArticleResponse;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HackathonSearcher extends ADataService {
    static Logger logger = LoggerFactory.getLogger(HackathonSearcher.class);

    public static void main(String[] args) throws IOException {
        String indexPath = "hackathon-index";
        HackathonSearcher newsSearcher = new HackathonSearcher();

        newsSearcher.port = 5678;
        newsSearcher.indexPath = indexPath;
        newsSearcher.parseArgsAndRun(args);
    }

    @Override
    public Article findArticleById(long id) throws TException {
        return super.findArticleById(id);
    }

    @Override
    public ArticleResponse searchArticle(ArticleRequest req) throws TException {

        logger.info(req.toString());

        String keyword = req.getKeyword();
        int offset = req.getOffset();


        TermQuery contentQuery = new TermQuery(new Term(CONTENT, keyword));

        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(contentQuery, BooleanClause.Occur.SHOULD);


        ArticleResponse response = new ArticleResponse();
        response.setRequest(req);
        List<Article> articles = new ArrayList<>();
        try {
            TopDocs topDocs = searcher.search(booleanQuery, 100);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            int length = scoreDocs.length;

            response.setTotal(length);

            logger.info("length : {}", length);
            for (int i = offset; i < length; i++) {
                int doc = scoreDocs[i].doc;
                Document document = searcher.doc(doc);
                String id = document.get(ID);
                String s = document.get(TITLE);
                String cotent = document.get(CONTENT);
                String url = document.get(URL);

                System.out.println(url);

                Article article = new Article();
                if (!StringUtils.isEmptyOrWhitespaceOnly(id)) {
                    article.setId(Integer.parseInt(id));
                }
                article.setUrl(url);
                article.setTitle(s);
                article.setContent(cotent);

                articles.add(article);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setArticles(articles);
        return response;
    }

    private Article randomArticle() {
        Article article = new Article();
        Random random = new Random();
        int i = random.nextInt();
        article.setTitle("this is the " + i + " title");
        article.setUrl("http://this is the " + i + " title");
        return article;
    }

}
