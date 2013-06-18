
package at.amarktl.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import at.amarktl.properties.Properties;

public class FileMonitor {

  private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
  private static WatchService watcher = null;

  static {
    try {
      watcher = FileSystems.getDefault().newWatchService();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void doit() {
    //define a folder root
    Path myDir = Paths.get(Properties.WEBHOME);

    try {
      WatchService watcher = myDir.getFileSystem().newWatchService();
      myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

      WatchKey watckKey = watcher.take();

      List<WatchEvent<?>> events = watckKey.pollEvents();
      for (WatchEvent<?> event : events) {
        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
          System.out.println("Created: " + event.context().toString());
        }
        if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
          System.out.println("Delete: " + event.context().toString());
        }
        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
          System.out.println("Modify: " + event.context().toString());
        }
      }

    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
    }
  }
}

//---------------------------- Revision History ----------------------------
//$Log$
//
