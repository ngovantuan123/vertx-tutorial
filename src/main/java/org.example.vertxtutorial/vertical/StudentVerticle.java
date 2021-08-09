package org.example.vertxtutorial.vertical;

import io.vertx.core.Promise;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.authorization.JWTAuthorization;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import org.example.vertxtutorial.service.StudentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentVerticle extends BaseRestVerticle {
    private static final String SERVICE_NAME = "tuangh-rest-api";
    private final StudentService studentService;
    private JWTAuth provider;
    private JWTAuthOptions jwtOpsAccessToken;
    private JWTAuthOptions jwtOpsRefreshToken;
    private JWTAuth providerForRefreshToken;
    public StudentVerticle(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        final Router router = Router.router(vertx);
        jwtOpsAccessToken =new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setBuffer("mypassword"));
        jwtOpsRefreshToken=new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setBuffer("tuangh-refresh-token"));
        //secure all api
        provider = JWTAuth.create(vertx,jwtOpsAccessToken);
        providerForRefreshToken=JWTAuth.create(vertx,jwtOpsRefreshToken);
        router.post("/login").handler(this::login);
        router.route("/api/*").handler(JWTAuthHandler.create(provider));
        //authorization
        JWTAuthorization authzProvider = JWTAuthorization.create("permissions");
        PermissionBasedAuthorization admin = PermissionBasedAuthorization.create("admin");
        AuthorizationHandler adminHandler = AuthorizationHandler.create(admin).addAuthorizationProvider(authzProvider);
        PermissionBasedAuthorization user = PermissionBasedAuthorization.create("user");
        AuthorizationHandler userHandler = AuthorizationHandler.create(user).addAuthorizationProvider(authzProvider);
        router.route("/api/admin*").handler(adminHandler);
        router.route("/api/user*").handler(userHandler);
        //custom same @Preauthorize spring security
//    ctx->{
//        provider.authenticate(new JsonObject().put("token",ctx.request().getHeader("Authorization")));
//        User user = ctx.user();
//        authzProvider.getAuthorizations(user).onComplete(ar -> {
//            if (ar.succeeded()) {
//                if (!admin.match(user)) {
//                    ctx.response().setStatusCode(403).end();
//                }
//            } else {
//                ctx.response().setStatusCode(403).end();
//            }
//        });
        router.route().handler(BodyHandler.create());
        router.get("/api/user/students").handler(this::getAllStudent);
        router.get("/api/user/students/:id").handler(this::getOneStudent);
        router.route("/api/students*").handler(BodyHandler.create());
        router.post("/api/students").handler(this::insertNewStudent);
        router.put("/api/students").handler(this::updateOneStudent);
        router.delete("/api/students/:id").handler(this::deleteOneStudent);
        router.post("/api/admin/unregister").handler(this::unregisterNoti);
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
        studentService.deleteOneStudent(routingContext.request().getParam("id"), deleteResultHandler(routingContext));

    }
    private void login(RoutingContext routingContext){
        HttpServerResponse response=routingContext.response();
        response.putHeader("content-type","application/json;charset=UTF8");
        Map<String,Object> responseBody = new HashMap<>();
        try{
            String userName = routingContext.request().getParam("username");
            String password = routingContext.request().getParam("password");
            String accesstoken = "";
            String refreshToken="";
            if(userName.equals("tuangh") && password.equals("123")){
                List<String> authorities = new ArrayList<>();
                //authorities.add("admin");
                authorities.add("user");
                accesstoken=provider.generateToken(new JsonObject().put("sub",userName),
                        new JWTOptions().setExpiresInMinutes(120).setPermissions(authorities));
                refreshToken=providerForRefreshToken.generateToken(new JsonObject().put("sub",userName));
                responseBody.put("accessToken","Bearer "+accesstoken);
                responseBody.put("refreshToken","Bearer "+refreshToken);
                response.end(Json.encodePrettily(responseBody));
            }else{
                response
                        .setStatusCode(401)
                        .end(Json.encodePrettily(new JsonObject().put("mesage","username password invalid")));
            }
        }catch (Exception e){
            response
                    .setStatusCode(500)
                    .end(Json.encodePrettily(e.getMessage()));
        }
    }
    private void updateOneStudent(RoutingContext routingContext) {
        JsonObject student = routingContext.getBodyAsJson();
        JsonObject responseBody = new JsonObject();
        responseBody.put("stastus", "200");
        responseBody.put("message", "update success");
        studentService.insertOrUpdateStudent(student, resultVoidHandler(routingContext, responseBody));
    }

    private void insertNewStudent(RoutingContext routingContext) {
        JsonObject student = routingContext.getBodyAsJson();
        JsonObject responseBody = new JsonObject();
        responseBody.put("stastus", "200");
        responseBody.put("message", "insert success");
        studentService.insertOrUpdateStudent(student, resultVoidHandler(routingContext, responseBody));
    }

    public void getAllStudent(RoutingContext routingContext) {
        studentService.getAllStudent(this.resultHandlerNonEmpty(routingContext));
    }

    public void getOneStudent(RoutingContext routingContext) {
        User _user = routingContext.user();
        System.out.println(_user.get("sub").toString());
        studentService.getOneStudent(routingContext.request().getParam("id"), this.resultHandlerNonEmpty(routingContext));

    }

    private void unregisterNoti(RoutingContext routingContext) {
        JsonObject body = new JsonObject(routingContext.getBody());
        studentService.unregisterNoti(body, this.resultHandlerNonEmpty(routingContext));
    }
}
