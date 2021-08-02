package org.example.vertxtutorial.Entity;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class test {
    public static void main(String[] args) {
        WebClient client = WebClient.create(Vertx.vertx());
        JsonObject body= new JsonObject();
        client
                .post(7914, "192.168.57.18", "/VietbankAPIs/v1/Noti/UnRegister")
                .sendJsonObject(body)
                .onSuccess(res -> {
                    // OK
                    JsonObject jsonObject = res.body().toJsonObject();
                    System.out.println("ok");
                })
                .onFailure(err ->
                         err.printStackTrace());
    }
}
