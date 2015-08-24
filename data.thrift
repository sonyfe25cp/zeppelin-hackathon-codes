namespace java com.zeppelin.hackathon.gen

// thrift -gen python data.thrift
// thrift -gen java data.thrift



struct Article{
  1: i64 id,
  2: string title,
  3: string content,
  4: string createdAt,
  5: string url,
  6: string publishDate,
  7: string provider,//网站名字，用汉字
  8: string html,//网页原文
}

struct ArticleRequest{
  1: string keyword,//MUST
  2: i32 offset = 0,
  3: i32 limit = 20,
  4: list<i64> ids,
  5: list<string> optionFilters,//OR
}


struct ArticleResponse{
  1: ArticleRequest request,
  2: list<Article> articles,
  3: i32 total,
}

struct HtmlObject{
  1: string html,
  2: string provider,
  3: string url,
  4: string refer,
  5: i32 statusCode,
  6: string redirectUrl,
}

service DataService{

  ArticleResponse searchArticle(1: ArticleRequest req) //查询文章

  Article findArticleById(1: i64 id) //查询文章
}


