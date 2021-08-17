package org.example.vertxtutorial.vertical;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.service.RouteToEBServiceHandler;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.rxjava3.ext.web.validation.ValidationHandler;
import io.vertx.serviceproxy.ServiceBinder;
import org.example.vertxtutorial.service.StudentService;

public class StudentServiceVerticle extends AbstractVerticle {
    HttpServer server;
    ServiceBinder serviceBinder;
    MessageConsumer<JsonObject> consumer;
    private void startStudentService() {
        serviceBinder = new ServiceBinder(vertx);
        // Create an instance of TransactionManagerService and mount to event bus
        StudentService studentService = StudentService.create(vertx);
        consumer = serviceBinder
                .setAddress("student_manager.myapp")
                .register(StudentService.class, studentService);
    }
    private Future<Void> startHttpServer() {
            return RouterBuilder.create(this.vertx, "student-service.json")
                    .onFailure(Throwable::printStackTrace) // In case the contract loading failed print the stacktrace
                    .compose(routerBuilder -> {
                        // Mount services on event bus based on extensions
                        routerBuilder.mountServicesFromExtensions();

                        // Generate the router
                        Router router = routerBuilder.createRouter();
                        router.errorHandler(400, ctx -> {
                            new HttpException(400,"bad request");
                        });
                        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost")).requestHandler(router);
                        return server.listen().mapEmpty();
                    });
        }
    @Override
    public void start(Promise<Void> promise) {
        startStudentService();
        startHttpServer().onComplete(promise);
    }
    @Override
    public void stop(){
        this.server.close();
        consumer.unregister();
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new StudentServiceVerticle());
    }
}
