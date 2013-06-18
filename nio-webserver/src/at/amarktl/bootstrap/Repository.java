
package at.amarktl.bootstrap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import at.amarktl.properties.Properties;

public class Repository {

  CacheManager cacheManager = CacheManager.create();
  String identifier = null;

  public Repository(String identifier) throws IOException {
    if (identifier == null) {
      throw new NullPointerException("'identifier' must not be null");
    }
    if (identifier.trim().length() == 0) {
      throw new IllegalArgumentException("'identifier' must not be empty");
    }
    this.identifier = identifier;
    cacheManager.addCache(this.identifier);
    init0();
  }

  private void init0() throws IOException {
    Files.walkFileTree(Paths.get(Properties.WEBHOME), new BootstrapLoader());
  }

  private final class BootstrapLoader extends SimpleFileVisitor<Path> {

    public BootstrapLoader() {
    }

    /** {@inheritDoc} */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      System.out.println("[" + identifier + "] loading file from URI [" + file + "]");

      if (!attrs.isRegularFile()) {
        return FileVisitResult.CONTINUE;
      }

      load(file);

      return FileVisitResult.CONTINUE;
    }

  }

  public byte[] load(Path file) throws FileNotFoundException, IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file.toFile()));
      StringBuilder response = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }

      byte[] b = response.toString().getBytes(Charset.forName("UTF-8"));
      System.out.println("[" + identifier + "] finished loading file from URI [" + file + "]");
      String path = file.toAbsolutePath().toString().replaceAll(Pattern.quote("\\"), "/");
      Element cached = new Element(path, b);
      Cache cache = cacheManager.getCache(identifier);
      cache.put(cached);

      System.out.println("[" + identifier + "] added file [" + path + "] to cache");
      return b;
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  public byte[] get(String path) throws IOException {
    //check if file is already in cache
    path = path.replaceAll(Pattern.quote("\\"), "/");

    Cache cache = cacheManager.getCache(identifier);
    System.out.println("[" + identifier + "] lookup file [" + path + "] in cache");
    Element element = cache.get(path);
    if (element != null) {
      System.out.println("[" + identifier + "] loaded file [" + path + "] from cache.");
      return (byte[]) element.getObjectValue();
    }
    return load(Paths.get(path));
  }
}

//---------------------------- Revision History ----------------------------
//$Log$
//
