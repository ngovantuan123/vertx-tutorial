package org.example.vertxtutorial.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.example.vertxtutorial.Entity.StudentEntity;

import java.util.HashMap;

public class StudentServiceImpl implements org.example.vertxtutorial.service.StudentService {
    private Vertx Vertx;
    public StudentServiceImpl (Vertx Vertx){
        this.Vertx =Vertx;
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
    public void unregisterNoti(JsonObject body, Handler<AsyncResult<Void>> resultHandler)  {
        System.out.println("unregister");
        try{

        }catch (Exception e){

        }
        resultHandler.handle(Future.succeededFuture());
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
