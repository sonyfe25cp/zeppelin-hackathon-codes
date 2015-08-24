package com.zeppelin.hackathon.engine.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AIndexBuilder extends AIndexService {
    static Logger logger = LoggerFactory.getLogger(AIndexBuilder.class);

    @Option(name = "-t", usage = "-t test mode for index, only index firt N number file")
    protected int t = 0;


    public AIndexBuilder() throws IOException {
    }

    void init() throws IOException {
        File folder = new File(indexPath);
        if (folder.exists()) {
            folder.delete();
            logger.info("delete old index folder");
        }
        Directory dir = NIOFSDirectory.open(folder);
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_48, analyzer);
        writer = new IndexWriter(dir, conf);

    }

    protected void doArgs(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(120);

        try {
            parser.parseArgument(args);
            if (this.help) {
                System.err.println("java {{cp}} " + this.getClass().getCanonicalName() + " [options...] arguments...");
                System.err.println("-t set index to test model, which only index few files");
                System.err.println("-p set the index path");
                parser.printUsage(System.err);
                System.exit(1);
            } else {
                init();
                build();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    protected IndexWriter writer;

    protected IDataProvider dataProvider;

    public void build() throws SQLException {
        logger.info("begin to index the data from provider");
        if (StringUtils.isEmpty(indexPath)) {
            logger.error("indexPath is null");
            System.exit(0);
        }
        if (dataProvider == null) {
            logger.error("dataProvider is null.");
            return;
        }
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(cpu, cpu, 1, TimeUnit.DAYS, new ArrayBlockingQueue<Runnable>(cpu * 2), new ThreadPoolExecutor.CallerRunsPolicy());
        List<Document> next = null;
        int count = 0;
        long start = System.currentTimeMillis();
//        do {
        next = dataProvider.next();
        for (Document document : next) {
            poolExecutor.submit(new DataConsumer(document));
            count++;
            if (count % 1000 == 0) {
                logger.info("add {} document to index", count);
            }
        }
//        } while (next != null && next.size() > 0);

        poolExecutor.shutdown();
        try {
            poolExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            writer.commit();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        logger.info("index over");
        logger.info("it cost {}ms to index {} files, rate:{}", new String[]{(end - start) + "", count + "", (count / (end - start + 0.0) * 1000) + ""});
    }

    private class DataConsumer implements Runnable {

        Document document;

        public DataConsumer(Document document) {
            this.document = document;
        }

        @Override
        public void run() {
            try {
                writer.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
