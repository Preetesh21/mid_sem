import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

class operationsTest {

    operations new_obj;
    @BeforeEach
    void setUp() throws IOException, SQLException, ParseException {
        new_obj=new operations();
        Map<Object, Object> map = new_obj.json_parser("/home/hp/Desktop/mid_sem_java/java/src/main/resources/config.json");
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Long port = (Long)map.get("Port");
        String host= (String) map.get("Host");

        // This body handler will be called for all routes
        router.route().handler(BodyHandler.create());
        new_obj.start_server(router);
        httpServer.requestHandler(router).listen(port.intValue(),host);

    }

    @AfterEach
    void tearDown() {
        new_obj=null;
    }

    @Test
    void test_server() {
        receiver.main();
        String message = sender.main();
        assert message.equals("Success");
    }
}