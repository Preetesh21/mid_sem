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
     * @return Object
     */
    Object start_server();


    /**
     * This is responsible for sending the message to the receiver
     * @return Object
     */
    Object send_request();


    /**
     * Execute an insert statement and return the number of rows
     affected.
     * @param queryId Unique ID of the query in the queries.xml file.
     * @param queryParam Parameter(s) to be used in the query.
     * @return an int
     */
    int insert(String queryId, Object queryParam);


    /**
     * Execute a select statement and return the desired values.
     * @param queryId Unique ID of the query in the queries.xml file.
     * @param queryParam Parameter(s) to be used in the query.
     * @return an Object
     */
    Object Select(String queryId, Object queryParam);

}
