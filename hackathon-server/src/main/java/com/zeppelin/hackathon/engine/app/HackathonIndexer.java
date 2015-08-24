package com.zeppelin.hackathon.engine.app;

import com.zeppelin.hackathon.engine.service.AIndexBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 建立索引
 *
 * @author ChenJie
 */
public class HackathonIndexer extends AIndexBuilder {
    static Logger logger = LoggerFactory.getLogger(HackathonIndexer.class);

    public HackathonIndexer() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        String indexPath = "hackathon-index";

        HackathonDataProvider newsDataProvider = new HackathonDataProvider();
        HackathonIndexer newsIndexer = new HackathonIndexer();
        newsIndexer.dataProvider = newsDataProvider;
        newsIndexer.indexPath = indexPath;
        newsIndexer.doArgs(args);
    }


}

