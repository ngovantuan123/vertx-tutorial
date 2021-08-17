package org.example.vertxtutorial.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.handler.HttpException;

public class StudentServiceImpl implements org.example.vertxtutorial.service.StudentService {
    private Vertx vertx;
    public StudentServiceImpl (Vertx vertx){
        this.vertx =vertx;
    }


    @Override
    public void unregisterNoti(JsonObject body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler)  {
        System.out.println("unregister");
        DeliveryOptions options = new DeliveryOptions();
        System.out.println("[send] sending data in " + Thread.currentThread().getName());
        this.vertx.eventBus().request("vietbank.noti.unregister", body, options, (res2) -> {
            if (res2.succeeded()) {
                System.out.println("Succ");
                System.out.println("============>" + ((Message)res2.result()).body().toString());
                resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson((JsonObject)(res2.result()).body())));
            } else {
                System.out.println("failure");
                new HttpException(500, "Something bad happened");
                resultHandler.handle(Future.failedFuture(res2.cause()));
            }

        });
    }

    @Override
    public void getAllStudent(Handler<AsyncResult<JsonObject>> resultHandler) {
        DeliveryOptions options = new DeliveryOptions();
        this.vertx.eventBus().request("vietbank.student",new JsonObject().put("type","all"), options, (res2) -> {
            if (res2.succeeded()) {
                System.out.println("Succ");
                System.out.println("============>" + ((Message)res2.result()).body().toString());
                resultHandler.handle(Future.succeededFuture((JsonObject)(res2.result()).body()));
            } else {
                System.out.println("failure");
                resultHandler.handle(Future.failedFuture(res2.cause()));
            }

        });
    }

    @Override
    public void getOneStudent(String id, Handler<AsyncResult<JsonObject>> resultHandler) {
        DeliveryOptions options = new DeliveryOptions();
        this.vertx.eventBus().request("vietbank.student",new JsonObject().put("type","one").put("id",id), options, (res2) -> {
            if (res2.succeeded()) {
                System.out.println("Succ");
                if(res2.result().body() ==null){
                    resultHandler.handle(Future.succeededFuture());
                }else{
                    System.out.println("============>" + ((Message)res2.result()).body().toString());
                    resultHandler.handle(Future.succeededFuture((JsonObject)(res2.result()).body()));
                }

            } else {
                System.out.println("failure");
                resultHandler.handle(Future.failedFuture(res2.cause()));
            }
        });
    }

    @Override
    public void insertOrUpdateStudent(JsonObject student, Handler<AsyncResult<Void>> resultHandler) {
        DeliveryOptions options = new DeliveryOptions();
        JsonObject message = new JsonObject().put("type","insert/update").put("data",student);
        this.vertx.eventBus().request("vietbank.student",message, options, (res2) -> {
            if (res2.succeeded()) {
                System.out.println("Succ");
                    System.out.println("============>" + ((Message)res2.result()).body().toString());
                    resultHandler.handle(Future.succeededFuture());
            } else {
                System.out.println("failure");
                resultHandler.handle(Future.failedFuture(res2.cause()));
            }

        });
    }

    @Override
    public void deleteOneStudent(String id, Handler<AsyncResult<Void>> resultHandler) {
        DeliveryOptions options = new DeliveryOptions();
        JsonObject message = new JsonObject().put("type","delete").put("id",id);
        this.vertx.eventBus().request("vietbank.student",message, options, (res2) -> {
            if (res2.succeeded()) {
                System.out.println("Succ");
                resultHandler.handle(Future.succeededFuture());
            } else {
                System.out.println("failure");
                resultHandler.handle(Future.failedFuture(res2.cause()));
            }

        });
    }
}
