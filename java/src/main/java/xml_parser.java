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
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><kyc>123</kyc><address>test</address><resiFI>adds</resiFI></user>";
        HashMap<String, String> values=get_xml(xmlString);
        System.out.println(values);
    }
}