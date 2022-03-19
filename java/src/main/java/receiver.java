import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class receiver {
    public static void receiver_server(Router router){
        router.post("/")
            .handler(routingContext -> {
                HttpServerResponse response = routingContext.response();
                String jsonObject = String.valueOf(routingContext.getBody());
                System.out.println(jsonObject);
                response.setStatusCode(200);
                response.setChunked(true);
                response.write("Success" );
                response.end();
            });
    }

    public static void main() {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        int port = 8080;
        String host= "127.0.0.1";
        // This body handler will be called for all routes
        router.route().handler(BodyHandler.create());
        receiver_server(router);
        httpServer.requestHandler(router).listen(port,host);
    }
}
