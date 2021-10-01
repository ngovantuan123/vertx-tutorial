package org.example.vertxtutorial.utils;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

public class DBUtils {
    private JDBCClient client;
    public DBUtils(Vertx vertx,JsonObject dbConfig){
        client = JDBCClient.create(vertx, dbConfig);
    }
    public JDBCClient getJdbcClient(){
        return client;
    }
}
