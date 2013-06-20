
package at.amarktl.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config implements FileChangeListener {

  private final static String WEB_SERVER_CFG = "web-server";
  private Element webServerNode;

  public Config(String filePath) throws FileNotFoundException {
    File file = new File(filePath);
    parseFile(file);
    FileMonitor.getInstance().addFileChangeListener(this, file, 2);
  }

  private void parseFile(File file) {
    try {

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);
      doc.getDocumentElement().normalize();

      NodeList nodes = doc.getElementsByTagName(WEB_SERVER_CFG);
      if (nodes == null || nodes.getLength() == 0) {
        throw new ParserConfigurationException("tag \"web-server\" not found!");
      }
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          webServerNode = (Element) node;
          break;
        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  public String getValueForTag(String tag) {
    Object value = getValue(tag);
    if (value instanceof String) {
      return (String) value;
    }
    return null;
  }

  public String getValueForTag(String tag, String defaultValue) {
    String value = getValue(tag);
    if (value == null) {
      return defaultValue;
    } else {
      return value;
    }
  }

  public int getValueForTag(String tag, int defaultValue) {
    String value = getValue(tag);
    if (value == null || value.length() == 0) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
    }

    return defaultValue;
  }

  private String getValue(String tag) {

    NodeList nodeTag = webServerNode.getElementsByTagName(tag);
    if (nodeTag == null || nodeTag.getLength() == 0) {
      return null;
    }
    NodeList nodes = nodeTag.item(0).getChildNodes();

    Node node = (Node) nodes.item(0);
    if (node == null) {
      return null;
    }

    return node.getNodeValue();
  }

  @Override
  public void fileChanged(File file) {
    parseFile(file);
  }

}
