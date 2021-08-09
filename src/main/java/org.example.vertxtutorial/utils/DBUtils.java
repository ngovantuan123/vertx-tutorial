package org.example.vertxtutorial.utils;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

public class DBUtils {
    private JDBCClient client;
    public DBUtils(Vertx vertx){
        JsonObject dbConfig = new JsonObject();
        dbConfig.put("url", "jdbc:oracle:thin:@//192.168.59.8:1521/tcbsweb");
        dbConfig.put("driver_class", "oracle.jdbc.driver.OracleDriver");
        dbConfig.put("user", "halong");
        dbConfig.put("password", "halong");
        client = JDBCClient.create(vertx, dbConfig);
    }
    public JDBCClient getJdbcClient(){
        return client;
    }
}
