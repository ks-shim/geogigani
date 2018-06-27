package dwayne.shim.geogigani.crawler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {

        final String xmlFileLocation = "D:\\workspace\\geogigani\\crawler\\src\\main\\resources\\test.xml";

        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(xmlFileLocation))) {
            String line = null;
            while((line = in.readLine()) != null) {
                line = line.replaceAll("&nbsp;", "");
                sb.append(line);
            }
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(sb.toString())));
        //doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("item");
        for(int i=0; i<nodeList.getLength(); i++) {
            Element element = (Element)nodeList.item(i);
            //System.out.println(element.getElementsByTagName("addr1").);
        }


    }
}
