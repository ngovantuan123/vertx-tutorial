package org.example.vertxtutorial;

import io.vertx.core.Vertx;
import org.example.vertxtutorial.service.StudentService;
import org.example.vertxtutorial.vertical.StudentVerticle;

public class DeployVerticle {
    public static void main(String[] args) {
        Vertx vertx=Vertx.vertx();
        StudentService studentService = StudentService.create(vertx);
        vertx.deployVerticle(new StudentVerticle(studentService));

    }
}
