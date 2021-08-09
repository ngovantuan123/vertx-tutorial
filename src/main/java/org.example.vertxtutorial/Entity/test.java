package org.example.vertxtutorial.Entity;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;

import java.util.List;

public class test {
    public static void main(String[] args) {
        JsonObject dbConfig = new JsonObject();
        Vertx vertx = Vertx.vertx();
        dbConfig.put("url", "jdbc:oracle:thin:@//192.168.59.8:1521/tcbsweb");
        dbConfig.put("driver_class", "oracle.jdbc.driver.OracleDriver");
        dbConfig.put("user", "halong");
        dbConfig.put("password", "halong");
        JDBCClient dbClient = JDBCClient.create(vertx, dbConfig);
        dbClient.query("select * from APIS_NOTI_REGISTER",queryRes -> {
            if(queryRes.succeeded()){

                ResultSet resultSet = queryRes.result();
                List<JsonObject> rows = resultSet.getRows();

                rows.forEach(System.out::println);

            }else{
                System.out.println ( "Error querying the database!");
            }
        });

        System.out.println("ss");
    }
}
