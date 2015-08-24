package com.zeppelin.hackathon.engine.service;

import com.zeppelin.hackathon.gen.DataService;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class AIndexSearcher<T> extends AIndexService implements DataService.Iface {
    static Logger logger = LoggerFactory.getLogger(AIndexSearcher.class);

    @Option(name = "-port", usage = "the port")
    protected int port = 5678;

    private void bindAndListen() throws TTransportException {
        if (this.port < 0) {
            return;
        }
        DataService.Processor<AIndexSearcher<T>> processor = new DataService.Processor<>(this);
        TNonblockingServerSocket socket = new TNonblockingServerSocket(this.port);
        TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(socket)
                .processor(processor).protocolFactory(new TBinaryProtocol.Factory());

        args.selectorThreads(1);
        args.workerThreads(this.cpu);

        TThreadedSelectorServer server = new TThreadedSelectorServer(args);
        logger.info("{} listen on {}", this.getClass().getSimpleName(), this.port);
        server.serve();
    }

    //用于被override
    //在所有事情最前面执行
    public void prepare() {

    }

    public void parseArgsAndRun(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(120);

        try {
            parser.parseArgument(args);
            if (this.help) {
                System.err.println("java {{cp}} " + this.getClass().getCanonicalName() + " [options...] arguments...");
                parser.printUsage(System.err);
                System.exit(1);
            } else {
                prepare();
                Directory dir = NIOFSDirectory.open(new File(getIndexPath()));

                searcher = new IndexSearcher(DirectoryReader.open(dir));
                reader = searcher.getIndexReader();
                atomicReader = SlowCompositeReaderWrapper.wrap(reader);
                after();
                this.bindAndListen();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected void after() {

    }

    protected IndexSearcher searcher;
    protected IndexReader reader;
    protected AtomicReader atomicReader;

}
