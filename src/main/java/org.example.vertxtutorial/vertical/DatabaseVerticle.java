package org.example.vertxtutorial.vertical;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import org.example.vertxtutorial.utils.DBUtils;

import java.util.List;
import java.util.UUID;

public class DatabaseVerticle extends AbstractVerticle {
    private JDBCClient client;
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        client= new DBUtils(this.vertx,config().getJsonObject("db")).getJdbcClient();
        MessageConsumer<JsonObject> consumer =  vertx.eventBus().consumer("vietbank.student");
        consumer.handler(message -> {
            System.out.println("received a message: " + message.body());
            if(message.body().getString("type").equals("all")){
                getAll(message);
            }else if(message.body().getString("type").equals("one")){
                getOne(message);
            }else if(message.body().getString("type").equals("insert/update")){
                insertUpdate(message);
            }else if(message.body().getString("type").equals("delete")){
                delete(message);
            }else{
                message.fail(400,"");
            }
        });

    }
    private void delete(Message<JsonObject> message) {
        client.updateWithParams("delete from STUDENT where STUDENT_ID=?",new JsonArray().add(message.body().getValue("id")),res->{
            if(res.succeeded()){
                message.reply(new JsonObject().put("message","ok"));
            }else {
                res.cause().printStackTrace();
                message.fail(500,res.cause().getMessage());
            }
        });
    }

    private void insertUpdate(Message<JsonObject> message) {
        String sql =null;
        JsonArray params=new JsonArray();
        if(message.body().getJsonObject("data").getString("id") == null){
            sql ="insert into STUDENT(STUDENT_ID,FULL_NAME,CLASS_NAME,BIRTHDAY) values(?,?,?,?)";
            params.add(UUID.randomUUID().toString());
            message.body().getJsonObject("data").stream().forEach(i->params.add(i.getValue()));
        }else{
            sql ="update STUDENT set FULL_NAME=?,CLASS_NAME=?,BIRTHDAY=? where STUDENT_ID=?";
            message.body().getJsonObject("data").stream().forEach(i->params.add(i.getValue()));
            params.add(params.getValue(0));
            params.remove(0);
        }
        client.updateWithParams(sql,params,res->{
            if(res.succeeded()){
                message.reply(new JsonObject().put("message","ok"));
            }else {
                res.cause().printStackTrace();
                message.fail(500,res.cause().getMessage());
            }
        });
    }

    private void getOne(Message<JsonObject> message) {
        if(message.body().getString("id") == null){
            message.reply(new JsonObject().put("message","id must be not null" ));
        }else {
            JsonArray param = new JsonArray().add(message.body().getString("id"));
            client.queryWithParams("select * from STUDENT where STUDENT_ID =? ", param, queryRes -> {
                if (queryRes.succeeded()) {
                    ResultSet resultSet = queryRes.result();
                    List<JsonObject> rows = resultSet.getRows();
                    rows.forEach(System.out::println);
                    if(rows.isEmpty()){
                        message.reply(null);
                    }else{
                        message.reply(rows.get(0));
                    }
                } else {
                    message.fail(500, queryRes.cause().getMessage());
                }
            });
        }
    }

    private void getAll(Message message){
            client.query("select * from STUDENT",queryRes -> {
                if(queryRes.succeeded()){
                    ResultSet resultSet = queryRes.result();
                    List<JsonObject> rows = resultSet.getRows();
                    rows.forEach(System.out::println);
                    message.reply(new JsonObject().put("data",rows));
                }else{
                    message.fail(500,queryRes.cause().getMessage());
                }});
    }
}
