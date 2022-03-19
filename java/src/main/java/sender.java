import net.dongliu.requests.Requests;
public class sender {

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
        //System.out.println(xml_parser.xml_to_json(xmlString));
        String host= "127.0.0.1:";
        String port= "5000";
        String destination ="http://"+host+port;
        String r= Requests.post(destination).body(xml_parser.xml_to_json(xmlString)).send().readToText();
        System.out.println(r);
    }
}
