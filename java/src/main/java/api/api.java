package api;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class api {
    public static void main(String[] args) {
        System.out.println("Hello World");
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Route handler1 = router
                .get("/")
                .handler(routingContext -> {
                    System.out.println("came to hello: " );
                    HttpServerResponse response = routingContext.response();
                    response.setChunked(true);
                    response.write("Hi" );
                    response.end();
                });
        httpServer.requestHandler(router).listen(8091,"127.0.0.1");
    }
}