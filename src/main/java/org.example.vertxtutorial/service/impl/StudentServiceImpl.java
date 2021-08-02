package org.example.vertxtutorial.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.example.vertxtutorial.Entity.StudentEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import io.vertx.ext.web.client.WebClient;
import org.example.vertxtutorial.utils.DateUtils;

public class StudentServiceImpl implements org.example.vertxtutorial.service.StudentService {
    private Vertx vertx;
    public static String SERVER_NOTI="192.168.57.18";
    public static int PORT=7914;
    public static String URL_API="/VietbankAPIs/v1/Noti/UnRegister";

    public StudentServiceImpl (Vertx vertx){
        this.vertx =vertx;
        createExampleData();
    }
    private static HashMap students=new HashMap();
    public  void createExampleData()
    {
        students.put("1",new StudentEntity("1","1","1","1"));
        students.put("2",new StudentEntity("2","2","1","1"));
        students.put("3",new StudentEntity("3","3","1","1"));
        students.put("4",new StudentEntity("4","4","1","1"));
    }

    @Override
    public void unregisterNoti(JsonObject body, Handler<AsyncResult<JsonObject>> resultHandler)  {
        System.out.println("unregister");
        WebClient client = WebClient.create(vertx);
        JsonObject trace =new JsonObject();
        trace.put("RequestID", UUID.randomUUID().toString());
        trace.put("RequestDateTime", DateUtils.date2Str(new Date(), "yyyy-MM-dd") + "T" + DateUtils.date2Str(new Date(), "HH:mm:ss") + "Z");
        trace.put("channelID","Dev");
        trace.put("clientID","DevService");
        body.put("trace",trace);

        try{
            client
                    .post(PORT, SERVER_NOTI, URL_API)
                    .sendJsonObject(body)
                    .onSuccess(res -> {
                        // OK
                        JsonObject jsonObject = res.body().toJsonObject();
                        resultHandler.handle(Future.succeededFuture(jsonObject));
                    })
                    .onFailure(err ->
                            resultHandler.handle(Future.failedFuture( err.getMessage())));
        }catch (Exception e){
            resultHandler.handle(Future.failedFuture(e.getMessage()));
        }
    }

    @Override
    public void getAllStudent(Handler<AsyncResult<JsonObject>> resultHandler) {
        if(students.isEmpty()){
            resultHandler.handle(Future.succeededFuture());
        }else{
            JsonObject result = new JsonObject(Json.encode(students));
            resultHandler.handle(Future.succeededFuture(result));
        }
    }

    @Override
    public void getOneStudent(String id, Handler<AsyncResult<JsonObject>> resultHandler) {

            StudentEntity studentFound = (StudentEntity) students.get(id);
            if(studentFound == null){
                resultHandler.handle(Future.succeededFuture());
            }else{
                JsonObject result = new JsonObject(Json.encode(studentFound));
                resultHandler.handle(Future.succeededFuture(result));
            }
    }

    @Override
    public void insertOrUpdateStudent(JsonObject student, Handler<AsyncResult<Void>> asyncResultHandler) {

    }

    @Override
    public void deleteOneStudent(String id, Handler<AsyncResult<Void>> resultHandler) {

    }
}
