package com.jxf.wx.api.message;

import com.jxf.wx.api.message.util.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peiyu
 */
public class NewsMsg extends BaseMsg {

	private static final long serialVersionUID = 1L;
	private static final int WX_MAX_SIZE = 10;
    private              int maxSize     = WX_MAX_SIZE;
    private List<ArticleMsg> articles;

    public NewsMsg() {
        this.articles = new ArrayList<ArticleMsg>(maxSize);
    }

    public NewsMsg(int maxSize) {
        setMaxSize(maxSize);
        this.articles = new ArrayList<ArticleMsg>(maxSize);
    }

    public NewsMsg(List<ArticleMsg> articles) {
        setArticles(articles);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        if (maxSize < WX_MAX_SIZE && maxSize >= 1) {
            this.maxSize = maxSize;
        }
        if (articles != null && articles.size() > this.maxSize) {
            articles = articles.subList(0, this.maxSize);
        }
    }

    public List<ArticleMsg> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleMsg> articles) {
        if (articles.size() > this.maxSize) {
            this.articles = articles.subList(0, this.maxSize);
        } else {
            this.articles = articles;
        }
    }

    public void add(String title) {
        add(title, null, null, null);
    }

    public void add(String title, String url) {
        add(title, null, null, url);
    }

    public void add(String title, String picUrl, String url) {
        add(new ArticleMsg(title, null, picUrl, url));
    }

    public void add(String title, String description, String picUrl, String url) {
        add(new ArticleMsg(title, description, picUrl, url));
    }

    public void add(ArticleMsg article) {
        if (this.articles.size() < maxSize) {
            this.articles.add(article);
        }
    }

    @Override
    public String toXml() {
        MessageBuilder mb = new MessageBuilder(super.toXml());
        mb.addData("MsgType", RespType.NEWS);
        mb.addTag("ArticleCount", String.valueOf(articles.size()));
        mb.append("<Articles>\n");
        for (ArticleMsg article : articles) {
            mb.append(article.toXml());
        }
        mb.append("</Articles>\n");
        mb.surroundWith("xml");
        return mb.toString();
    }

}
