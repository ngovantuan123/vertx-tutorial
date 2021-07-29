package vertical;

import Entity.StudentEntity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StudentVertical extends AbstractVerticle {
    private static HashMap students=new HashMap();
    public  void createExampleData()
    {
        students.put("1",new StudentEntity("1","1","1","1"));
        students.put("2",new StudentEntity("2","2","1","1"));
        students.put("3",new StudentEntity("3","3","1","1"));
        students.put("4",new StudentEntity("4","4","1","1"));
    }
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        EventBus eb = vertx.eventBus();

        MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
        consumer.handler(message -> {
            System.out.println("I have received a message: " + message.body());
        });
        createExampleData();
        Router router=Router.router(vertx);
        router.get("/api/students").handler(this::getAllStudent);
        router.get("/api/students/:id").handler(this::getOneStudent);
        router.route("/api/students*").handler(BodyHandler.create());
        router.post("/api/students").handler(this::insertNewStudent);
        router.put("/api/students").handler(this::updateOneStudent);
        router.delete("/api/students/:id").handler(this::deleteOneStudent);
        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                startPromise.complete();
                            } else {
                                startPromise.fail(result.cause());
                            }
                        }
                );
    }

    private void deleteOneStudent(RoutingContext routingContext) {
        HttpServerResponse response=routingContext.response();
        response.putHeader("content-type","application/json;charset=UTF8");
        Map<String,Object> responseBody = new HashMap<>();
        try {
            String id = routingContext.request().getParam("id");
            StudentEntity oldStudent = (StudentEntity) students.get(id);
            if(oldStudent != null) {
                students.remove(oldStudent.getStudentId());
                responseBody.put("status","success");
                responseBody.put("message","delete success");
            }else{
                responseBody.put("status","fail");
                responseBody.put("message","student not found");
                response.setStatusCode(400);
            }
            response.end(Json.encodePrettily(responseBody));
        }
        catch (Exception ex)
        {
            response.end(Json.encodePrettily(ex.getMessage()));
        }

    }

    private void updateOneStudent(RoutingContext routingContext) {
        HttpServerResponse response=routingContext.response();
        response.putHeader("content-type","application/json;charset=UTF8");
        Map<String,Object> responseBody = new HashMap<>();
        try {
            StudentEntity stu=Json.decodeValue(routingContext.getBody(),StudentEntity.class);
            StudentEntity oldStudent = (StudentEntity) students.get(stu.getStudentId());
            if(oldStudent != null) {
                students.replace(oldStudent.getStudentId(),stu);
                responseBody.put("status","success");
                responseBody.put("message","update success");
            }else{
                responseBody.put("status","fail");
                responseBody.put("message","student not found");
                response.setStatusCode(400);
            }
            response.end(Json.encodePrettily(responseBody));
        }
        catch (Exception ex)
        {
            response.end(Json.encodePrettily(ex.getMessage()));
        }
    }

    private void insertNewStudent(RoutingContext routingContext) {
        HttpServerResponse response=routingContext.response();
        response.putHeader("content-type","application/json;charset=UTF8");
        Map<String,Object> responseBody = new HashMap<>();
        try {
            StudentEntity stu=Json.decodeValue(routingContext.getBody(),StudentEntity.class);
            students.put(stu.getStudentId(),stu);
            responseBody.put("status","success");
            responseBody.put("message","insert success");
            response.end(Json.encodePrettily(responseBody));
        }
        catch (Exception ex)
        {
            response.end(Json.encodePrettily(ex.getMessage()));
        }
    }

    public void getAllStudent(RoutingContext routingContext){
        HttpServerResponse response=routingContext.response();
        response.putHeader("content-type","application/json;charset=UTF-8");
        String sort = routingContext.request().getParam("sort");
        if(sort != null && sort.equalsIgnoreCase("desc") ){
            ArrayList sortedKeys = new ArrayList(students.keySet());
            Collections.sort(sortedKeys);
            if(sort.equalsIgnoreCase("desc"))
            {
                Collections.reverse(sortedKeys);
            }
            ArrayList sortEmployees=new ArrayList();
            for (Object key : sortedKeys)
            {
                sortEmployees.add(students.get(key));
            }
            response.end(Json.encodePrettily(sortEmployees));
        }
        else if(sort != null && sort.equalsIgnoreCase("asc")) {

        }
        else {
            response.end(Json.encodePrettily(students.values()));
        }
    }
    public void getOneStudent(RoutingContext routingContext){
        HttpServerResponse response=routingContext.response();
        response.putHeader("content-type","application/json;charset=UTF-8");
        String id = routingContext.request().getParam("id");
        Map<String,Object> responseBody = new HashMap<>();
        if(id == null || id.isEmpty()){
            responseBody.put("status","fail");
            responseBody.put("message","bad request");
            response.setStatusCode(400);
        }else{
            StudentEntity studentFound = (StudentEntity) students.get(id);
            if(studentFound == null){
                responseBody.put("status","success");
                responseBody.put("message","resource not found");
                response.setStatusCode(404);
            }else{
                responseBody.put("status","Success");
                responseBody.put("data",studentFound);
            }
            response.end(Json.encodePrettily(responseBody));
        }

    }
}
