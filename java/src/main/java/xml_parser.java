import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class xml_parser {
    public static String xml_to_json(String xmlString){
        try {
            JSONObject json = XML.toJSONObject(xmlString);
            //System.out.println(jsonString);
            return json.toString(4);
        }catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}