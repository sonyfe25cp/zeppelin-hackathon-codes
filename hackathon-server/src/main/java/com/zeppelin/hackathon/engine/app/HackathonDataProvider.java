package com.zeppelin.hackathon.engine.app;

import com.zeppelin.hackathon.engine.service.AIndexService;
import com.zeppelin.hackathon.engine.service.IDataProvider;
import com.zeppelin.hackathon.gen.Article;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OmarTech on 15-8-23.
 */
public class HackathonDataProvider implements IDataProvider {
    @Override
    public List<Document> trans2Doc(List list) {

        List<Document> documents = new ArrayList<>();

        for (Object object : list) {
            Article article = (Article) object;
            Document document = new Document();
            document.add(new StringField(AIndexService.ID, article.getId() + "", Field.Store.YES));
            document.add(new TextField(AIndexService.TITLE, article.getTitle(), Field.Store.YES));
            document.add(new StringField(AIndexService.URL, article.getUrl(), Field.Store.YES));
            document.add(new TextField(AIndexService.CONTENT, article.getContent(), Field.Store.YES));
            documents.add(document);
        }
        return documents;
    }

    @Override
    public List<Document> next() throws SQLException {
        List list = findList(null, 0, 0);
        List<Document> documents = trans2Doc(list);
        return documents;
    }

    @Override
    public List findList(Connection connection, long begin, long offset) throws SQLException {
        File folder = new File("/Users/omar/workspace/zeppelin-hackathon/hackathon-server/src/main/resources/zeppelin");
        List list = new ArrayList();
        if (folder.exists()) {
            try {
                for (File file : folder.listFiles()) {
                    List<String> lines = FileUtils.readLines(file);
                    Article article = new Article();
                    StringBuilder sb = new StringBuilder();
                    String name = file.getName();
                    int id = Integer.parseInt(name);
                    String title = null;
                    String url = null;
                    for (int i = 0; i < lines.size(); i++) {
                        if (i == 0) {
                            title = lines.get(i);
                        } else if (i == 1) {
                            url = lines.get(i);
                        }
                        sb.append(lines.get(i));
                    }
                    if (StringUtils.isEmpty(id + "")) {
                        id = 0;
                    }
                    article.setId(id);
                    if (StringUtils.isEmpty(title)) {
                        title = "This is a default title";
                    }
                    if (StringUtils.isEmpty(url)) {
                        url = "default url";
                    }
                    article.setTitle(title);
                    article.setUrl(url);
                    article.setContent(sb.toString());

                    System.out.println(id + "\t" + url);

                    list.add(article);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
