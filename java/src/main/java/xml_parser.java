import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class xml_parser {
    public static HashMap<String, String> get_xml(String xmlString) {
        HashMap<String, String> values = new HashMap<>();
        Document xml = convertStringToDocument(xmlString);
        assert xml != null;
        Node user = xml.getFirstChild();
        NodeList children = user.getChildNodes();
        Node child;
        for (int i = 0; i < children.getLength(); i++) {
            child = children.item(i);
            System.out.println(child.getNodeName());
            System.out.println(child.getTextContent());
            values.put(child.getNodeName(), child.getTextContent());
        }
        return values;

    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(
                    xmlStr)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String xmlString = "<Message>\n" +
                "    <!-- It is the service’s endpoint URL. -->\n" +
                "    <Sender>http://127.0.0.1:58572/</Sender>\n" +
                "    <!-- A string that defines a message type. -->\n" +
                "    <MessageType>Validate</MessageType>\n" +
                "    <!-- A  universally unique ID for a message. -->\n" +
                "    <MessageUUID>573fbfa0-97e7-11ec-8fbc-bf1589110003</MessageUUID>\n" +
                "    <!-- This is the main payload of the message.\n" +
                "    Its content will always be a CDATA section. -->\n" +
                "    <Body>\n" +
                "        <![CDATA[\n" +
                "       GOOG,INFY,AAPL\n" +
                "       ]]>\n" +
                "    </Body>\n" +
                "</Message>";
        HashMap<String, String> values=get_xml(xmlString);
        System.out.println(values);
    }
}