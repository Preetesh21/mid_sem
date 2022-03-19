import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import net.dongliu.requests.Requests;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class operations implements Message_Router {
    static Connection connection;
    public static void connect(String url) {
        Connection conn;
        try {
            // create a connection to the database
            String db_url="jdbc:sqlite:"+url;
            conn = DriverManager.getConnection(db_url);
            System.out.println("Connection to SQLite has been established.");
            connection = conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public  Map<Object, Object> json_parser(String json_file) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(json_file));
            JSONObject jsonObject = (JSONObject)obj;
            String host = (String)jsonObject.get("host");
            Long port = (Long)jsonObject.get("port");
            String db_url = (String)jsonObject.get("db_url");
            String log_file = (String)jsonObject.get("log_file");
            Map<Object, Object> map = new HashMap<>();
            map.put("Host",host);
            map.put("Port",port);
            map.put("DB URL",db_url);
            map.put("Log File",log_file);
            connect(db_url);
            return map;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start_server(Router router) {
        router.post("/")
                .handler(routingContext -> {
                    HttpServerResponse response = routingContext.response();
                    JsonObject jsonObject = routingContext.getBodyAsJson();
                    System.out.println(jsonObject.fieldNames());
                    JsonObject jsonObject2 = (jsonObject.getJsonObject("Message"));
                    String sender = jsonObject2.getString("Sender");
                    String MessageType = jsonObject2.getString("MessageType");
                    String body = jsonObject2.getString("Body");
                    response.setStatusCode(200);
                    response.setChunked(true);
                    response.write("Success" );
                    response.end();
                    System.out.println(sender + MessageType + body);
                    helper(router,sender,MessageType,body);
                });
    }

    @Override
    public Object send_request(Router router,String body,String destination) {
        return Requests.post(destination).body(body).send().readToText();
    }

    @Override
    public void helper(Router router, String queryId, String messageType,String body){
        CompletableFuture.runAsync(() -> {
            Map<Object, Object> map = Select(queryId,messageType);
            System.out.println(map);
            int resp1 = insert( Integer.toString((Integer) map.get("routeID")),"Received");
            System.out.println(resp1);
            // Send the Request
            Object resp3 = send_request(router,body, (String) map.get("Destination"));
            System.out.println(resp3);
            // Add message log again
            int resp2 = insert( Integer.toString((Integer) map.get("routeID")),"Sent");
            System.out.println(resp2);
        });
    }

    @Override
    public int insert(String routeId, String EventType) {
        AtomicInteger count = new AtomicInteger();
         String sql = "INSERT INTO message_logs(RouteID, EventType, EventTime) VALUES (?, ?, ?)";
         // set the values
            try{
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1,routeId);
                pstmt.setString(2,EventType);
                System.out.println(Timestamp.from(Instant.now()));
                pstmt.setString(3, String.valueOf(Timestamp.from(Instant.now())));
                count.set(pstmt.executeUpdate());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return count.get();
    }

    @Override
    public Map<Object, Object> Select(String sender, String messageType) {
        Map<Object, Object> map = new HashMap<>();
            // method call or code to be async.
            String sql = "select * from routing where Sender = ? AND MessageType = ?";

            try {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                // set the value
                pstmt.setString(1, sender);
                pstmt.setString(2,messageType);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    System.out.println(rs.getString("Destination"));
                    map.put("Destination",rs.getString("Destination"));
                    map.put("routeID",rs.getInt("RouteID"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return map;
    }

    public static void main(String[] args) {
        operations operation =  new operations();
        Map<Object, Object> map = operation.json_parser("/home/hp/Desktop/mid_sem_java/java/src/main/java/config.json");
        System.out.println("Host: " + map.get("Host"));
        System.out.println("Port: " + map.get("Port"));
        System.out.println("DB URL:"+ map.get("DB URL"));
        System.out.println("Log File:"+ map.get("Log File"));
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Long port = (Long)map.get("Port");
        String host= (String) map.get("Host");

        // This body handler will be called for all routes
        router.route().handler(BodyHandler.create());
        operation.start_server(router);
        httpServer.requestHandler(router).listen(port.intValue(),host);

    }
}
