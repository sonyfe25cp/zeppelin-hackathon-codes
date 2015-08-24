package com.zeppelin.hackathon.engine.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.kohsuke.args4j.Option;

public class AIndexService {
    @Option(name = "-cpu", usage = "-cpu the num of cpu")
    protected int cpu = Runtime.getRuntime().availableProcessors();

    @Option(name = "-p", usage = "-p index path")
    protected String indexPath;

    @Option(name = "-help", usage = "show the help")
    protected boolean help = false;

    protected Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String URL = "url";

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }
}
