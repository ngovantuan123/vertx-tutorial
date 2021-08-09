package org.example.vertxtutorial;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.example.vertxtutorial.service.StudentService;
import org.example.vertxtutorial.vertical.DatabaseVerticle;
import org.example.vertxtutorial.vertical.NotiVerticle;
import org.example.vertxtutorial.vertical.StudentVerticle;

public class DeployVerticle {
    public static void main(String[] args) {
        Vertx vertx=Vertx.vertx();
        ConfigStoreOptions defauStoreOptions = new ConfigStoreOptions()
                                                        .setType("file")
                                                        .setFormat("json")
                                                        .setConfig(new JsonObject().put("path","config.json"));
        ConfigRetrieverOptions opts=new ConfigRetrieverOptions()
                                        .addStore(defauStoreOptions);
        ConfigRetriever cfgRetriever = ConfigRetriever.create(vertx,opts);
        cfgRetriever.getConfig(jsonObjectAsyncResult -> {
                if(jsonObjectAsyncResult.succeeded()){
                    DeploymentOptions ops =new DeploymentOptions().setConfig(new JsonObject());
                    StudentService studentService = StudentService.create(vertx);
                    Future<String> student =Future.future(promise ->vertx.deployVerticle(new StudentVerticle(studentService)) );
                    Future<String> noti =Future.future(promise ->vertx.deployVerticle(new NotiVerticle(),new DeploymentOptions().setWorker(true)) );
                    Future<String> db =Future.future(promise ->vertx.deployVerticle(new DatabaseVerticle()) );
                }else {
                    System.out.println("can't load config....");
                }
        });


    }
}
