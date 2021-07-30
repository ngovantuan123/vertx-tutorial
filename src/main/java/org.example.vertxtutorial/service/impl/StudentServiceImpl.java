package org.example.vertxtutorial.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class StudentServiceImpl implements org.example.vertxtutorial.service.StudentService {
    private Vertx Vertx;
    public StudentServiceImpl (Vertx Vertx){
        this.Vertx =Vertx;
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
    }

    @Override
    public void getOneStudent(String id, Handler<AsyncResult<JsonObject>> resultHandler) {
    }

    @Override
    public void insertOrUpdateStudent(JsonObject student, Handler<AsyncResult<Void>> asyncResultHandler) {
    }

    @Override
    public void deleteOneStudent(String id, Handler<AsyncResult<Void>> resultHandler) {
    }
}
