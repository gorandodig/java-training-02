
package at.amarktl.http;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HTTPResponse {

  private String version = "HTTP/1.1";
  private int responseCode = 200;
  private String responseReason = "OK";
  private Map<String, String> headers = new LinkedHashMap<String, String>();
  private byte[] content;

  void addDefaultHeaders() {
    getHeaders().put("Date", new Date().toString());
    getHeaders().put("Server", "Java NIO Webserver");
    getHeaders().put("Connection", "close");
    getHeaders().put("Content-Length", Integer.toString(content.length));
  }

  public int getResponseCode() {
    return responseCode;
  }

  public String getResponseReason() {
    return responseReason;
  }

  public String getHeader(String header) {
    return getHeaders().get(header);
  }

  public byte[] getContent() {
    return content;
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  public void setResponseReason(String responseReason) {
    this.responseReason = responseReason;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public void setHeader(String key, String value) {
    getHeaders().put(key, value);
  }

  public String getVersion() {
    return version;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Entry<String, String> header : headers.entrySet()) {
      s.append(header.getKey() + "\t = " + header.getValue()+"\n");
    }
    return s.toString();
  }
}