import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.*;
import org.json.simple.parser.*;

public class operations implements Message_Router {
    @Override
    public Map<Object, Object> json_parser(String json_file) {
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
            return map;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object start_server() {
        return null;
    }

    @Override
    public Object send_request() {
        return null;
    }

    @Override
    public int insert(String queryId, Object queryParam) {
        return 0;
    }

    @Override
    public Object Select(String queryId, Object queryParam) {
        return null;
    }

    public static void main(String[] args) {
        operations operation =  new operations();
        Map<Object, Object> map = operation.json_parser("/home/hp/Desktop/mid_sem_java/java/src/main/java/config.json");
        System.out.println("Host: " + map.get("Host"));
        System.out.println("Port: " + map.get("Port"));
        System.out.println("DB URL:"+ map.get("DB URL"));
        System.out.println("Log File:"+ map.get("Log File"));
    }
}
