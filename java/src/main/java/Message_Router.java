import io.vertx.ext.web.Router;

import java.util.Map;

public interface Message_Router {

    /**
     * This function is responsible for taking a Json File as input and then returning the vital
     * information present inside it.
     * @param json_file File location having the contents
     * @return Object a Map instance
     **/
    Map<Object, Object> json_parser(String json_file);


    /**
     * This is responsible for creating a server and then listening on it from sender
     */
    void start_server(Router router);


    /**
     * This is responsible for sending the message to the receiver
     * @return Object
     */
    Object send_request(Router router,String body,String destination);

    void helper(Router router, String queryId, String messageType,String body);
    /**
     * Execute an insert statement and return the number of rows
     affected.
     * @param queryId Unique ID of the query in the queries.xml file.
     * @return an int
     */
    int insert(String queryId, String EventType);


    /**
     * Execute a select statement and return the desired values.
     * @param queryId Unique ID of the query in the queries.xml file.
     * @return an Object
     */
    Map<Object, Object> Select(String queryId, String messageType);

}
