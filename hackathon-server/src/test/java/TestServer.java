import com.zeppelin.hackathon.client.ClientException;
import com.zeppelin.hackathon.client.DataClients;
import com.zeppelin.hackathon.gen.Article;
import com.zeppelin.hackathon.gen.ArticleRequest;
import com.zeppelin.hackathon.gen.ArticleResponse;

import java.util.List;

/**
 * Created by OmarTech on 15-8-23.
 */
public class TestServer {
    public static void main(String[] args) {
        DataClients dataClients = new DataClients("172.16.5.239:5678,172.16.5.239:5678");
        ArticleRequest req = new ArticleRequest();
        req.setKeyword("install");
        req.setOffset(0);
        try {
            ArticleResponse response = dataClients.searchArticle(req);
            List<Article> articles = response.getArticles();
            for (Article article : articles) {
                System.out.println(article);
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
