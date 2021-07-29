import io.vertx.core.Vertx;
import vertical.StudentVertical;

public class DeployVertical {
    public static void main(String[] args) {
        Vertx vertx=Vertx.vertx();
        vertx.deployVerticle(new StudentVertical());

    }
}
