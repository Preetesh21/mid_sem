import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import net.dongliu.requests.Requests;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

public class operations implements Message_Router {
    static Connection connection;
    static FileHandler fh;
    public operations() throws IOException {
    fh = new FileHandler("/home/hp/Desktop/mid_sem_java/java/src/main/resources/LogFile.log");
    }

    public static void connect(String url) throws SQLException {
        Connection conn;
        // create a connection to the database
        String db_url="jdbc:sqlite:"+url;
        conn = DriverManager.getConnection(db_url);
        //System.out.println("Connection to SQLite has been established.");
        connection = conn;
        logging.writeLog(fh,"Connection Established");
    }

    @Override
    public  Map<Object, Object> json_parser(String json_file) throws IOException, ParseException, SQLException {
        JSONParser parser = new JSONParser();

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
            logging.writeLog(fh,"JSON Parsing done");
            return map;
    }

    @Override
    public void start_server(Router router) {
        router.post("/")
                .handler(routingContext -> {
                    HttpServerResponse response = routingContext.response();
                    JsonObject jsonObject = routingContext.getBodyAsJson();
                    //System.out.println(jsonObject.fieldNames());
                    JsonObject jsonObject2 = (jsonObject.getJsonObject("Message"));
                    String sender = jsonObject2.getString("Sender");
                    String MessageType = jsonObject2.getString("MessageType");
                    String body = jsonObject2.getString("Body");
                    logging.writeLog(fh,"Message Received");
                    helper(router,sender,MessageType,body);
                    response.setStatusCode(200);
                    response.setChunked(true);
                    response.write("Success" );
                    response.end();
                    //System.out.println(sender + MessageType + body);
                });
    }

    @Override
    public Object send_request(Router router,String body,String destination) {
        logging.writeLog(fh,"Message Sent to the Receiver");
        return Requests.post(destination).body(body).send().readToText();
    }

    @Override
    public void helper(Router router, String queryId, String messageType,String body){
       // CompletableFuture.runAsync(() -> {
            Map<Object, Object> map = Select(queryId,messageType);
            //System.out.println(map);
            logging.writeLog(fh,"Found Destination from DB");
            insert( Integer.toString((Integer) map.get("routeID")),"Received");
            //System.out.println(resp1 + "hello wor");
            logging.writeLog(fh,"Added Received log in the DB");
            // Send the Request
            Object resp3 = send_request(router,body, (String) map.get("Destination"));
            System.out.println("Message Sent "+resp3);
            logging.writeLog(fh,"Message Sent");
            // Add message log again
            insert( Integer.toString((Integer) map.get("routeID")),"Sent");
            //System.out.println(resp2);
            logging.writeLog(fh,"Added Sent Log in the DB");
        //});
    }

    @Override
    public void insert(String routeId, String EventType) {
         String sql = "INSERT INTO message_logs(RouteID, EventType, EventTime) VALUES (?, ?, ?)";
         // set the values
            try{
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1,routeId);
                pstmt.setString(2,EventType);
                //System.out.println(Timestamp.from(Instant.now()));
                pstmt.setString(3, String.valueOf(Timestamp.from(Instant.now())));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
                    //System.out.println(rs.getString("Destination"));
                    map.put("Destination",rs.getString("Destination"));
                    map.put("routeID",rs.getInt("RouteID"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return map;
    }
}
