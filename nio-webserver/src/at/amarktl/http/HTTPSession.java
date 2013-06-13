
package at.amarktl.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Map;

public final class HTTPSession {

  private final SocketChannel channel;
  private final ByteBuffer buffer = ByteBuffer.allocate(2048);
  private final StringBuilder readLines = new StringBuilder();
  private int mark = 0;

  public HTTPSession(SocketChannel channel) {
    this.channel = channel;
  }

  /**
   * Try to read a line.
   */
  public String readLine() throws IOException {
    StringBuilder sb = new StringBuilder();
    int l = -1;
    while (buffer.hasRemaining()) {
      char c = (char) buffer.get();
      sb.append(c);
      if (c == '\n' && l == '\r') {
        // mark our position
        mark = buffer.position();
        // append to the total
        readLines.append(sb);
        // return with no line separators
        return sb.substring(0, sb.length() - 2);
      }
      l = c;
    }
    return null;
  }

  /**
   * Get more data from the stream.
   */
  public void readData() throws IOException {
    buffer.limit(buffer.capacity());
    int read = channel.read(buffer);
    if (read == -1) {
      throw new IOException("End of stream");
    }
    buffer.flip();
    buffer.position(mark);
  }

  private void writeLine(String line) throws IOException {
    channel.write(ByteBuffer.wrap((line + "\r\n").getBytes(Charset.forName("UTF-8"))));
  }

  public void sendResponse(HTTPResponse response) {
    response.addDefaultHeaders();
    try {
      writeLine(response.getVersion() + " " + response.getResponseCode() + " " + response.getResponseReason());
      for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
        writeLine(header.getKey() + ": " + header.getValue());
      }
      writeLine("");
      channel.write(ByteBuffer.wrap(response.getContent()));
    } catch (IOException ex) {
      // slow silently
    }
  }

  public void close() {
    try {
      channel.close();
    } catch (IOException ex) {
    }
  }

  public String getContent() {
    return readLines.toString();
  }

}
