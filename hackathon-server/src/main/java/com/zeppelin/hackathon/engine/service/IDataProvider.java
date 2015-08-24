package com.zeppelin.hackathon.engine.service;

import org.apache.lucene.document.Document;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IDataProvider<T> {

    List<Document> trans2Doc(List<T> list);

    List<Document> next() throws SQLException;

    List findList(Connection connection, long begin, long offset) throws SQLException;
}
