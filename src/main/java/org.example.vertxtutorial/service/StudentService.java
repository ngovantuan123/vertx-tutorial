package org.example.vertxtutorial.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.example.vertxtutorial.service.impl.StudentServiceImpl;

@VertxGen
@ProxyGen
public interface StudentService {
    String SERVICE_NAME = "noti-eb-service";
    String SERVICE_ADDRESS = "service.noti";

    static StudentServiceImpl create (Vertx Vertx){
        return new StudentServiceImpl(Vertx);

    }
    void unregisterNoti(JsonObject body,Handler<AsyncResult<JsonObject>> resultHandler );
    void getAllStudent(Handler<AsyncResult<JsonObject>> resultHandler);
    void getOneStudent(String id,Handler<AsyncResult<JsonObject>> resultHandler);
    void insertOrUpdateStudent(JsonObject student,Handler<AsyncResult<Void>> asyncResultHandler);
    void deleteOneStudent(String id ,Handler<AsyncResult<Void>> resultHandler);
}
