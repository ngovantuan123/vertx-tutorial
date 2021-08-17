package org.example.vertxtutorial.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import org.example.vertxtutorial.service.impl.StudentServiceImpl;

@WebApiServiceGen
@ProxyGen // Generate the proxy and handler
@VertxGen // Generate clients in non-java languages
public interface StudentService {
    static StudentServiceImpl create (Vertx Vertx){
        return new StudentServiceImpl(Vertx);
    }
    void unregisterNoti(JsonObject body,ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler );
    void getAllStudent(Handler<AsyncResult<JsonObject>> resultHandler);
    void getOneStudent(String id,Handler<AsyncResult<JsonObject>> resultHandler);
    void insertOrUpdateStudent(JsonObject student,Handler<AsyncResult<Void>> asyncResultHandler);
    void deleteOneStudent(String id ,Handler<AsyncResult<Void>> resultHandler);
    static StudentService createProxy(Vertx vertx, String address) {
        return new ServiceProxyBuilder(vertx)
                .setAddress(address)
                .build(StudentService.class);
        // Alternatively, you can create the proxy directly using:
        // return new ProcessorServiceVertxEBProxy(vertx, address);
        // The name of the class to instantiate is the service interface + `VertxEBProxy`.
        // This class is generated during the compilation
    }

}
