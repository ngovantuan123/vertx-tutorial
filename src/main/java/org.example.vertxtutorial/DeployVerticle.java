package org.example.vertxtutorial;

import io.vertx.core.Vertx;
import org.example.vertxtutorial.vertical.StudentVertical;

public class DeployVertical {
    public static void main(String[] args) {
        Vertx vertx=Vertx.vertx();
        vertx.deployVerticle(new StudentVertical());

    }
}
