import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.example.vertxtutorial.DeployVerticle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MyTest {
    private Vertx vertx;
    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(DeployVerticle.class.getName(),
                context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }
    @Test
    public void testMyApplication(TestContext context) {
        final Async async = context.async();

//        vertx.createHttpClient().getNow(8080, "localhost", "/",
//                response -> {
//                    response.handler(body -> {
//                        context.assertTrue(body.toString().contains("Hello World!"));
//                        async.complete();
//                    });
//                });
    }
}
