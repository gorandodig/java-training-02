
package at.amarktl.cluster;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import at.amarktl.bootstrap.Repository;
import at.amarktl.properties.Properties;

public class Node extends UnicastRemoteObject implements IClusterNode {

  private static final String RMI_IDENTIFIER_NODE = "cluster-node";
  private static final String RMI_IDENTIFIER_MASTER = "web-srv-master";
  Repository repository = null;
  private static final long serialVersionUID = 1L;
  int port;
  String address;
  String name;

  private ExecutorService handles = null;

  /** {@inheritDoc} */
  @Override
  public String getIdentifier() throws RemoteException {
    return name + "@" + address + ":" + port;
  }

  @Override
  public void connect(String server, int serverport) throws RemoteException {
    initRMI();

    System.out.println("Cluster Node [" + getIdentifier() + "] initialized");

    register(server, serverport);
  }

  private void initRMI() {
    try {
      Registry registry = LocateRegistry.createRegistry(port);
      registry.rebind(RMI_IDENTIFIER_NODE, this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void register(String host, int port) {
    try {
      System.out.println("Trying to register Cluster Node [" + name + "@" + address + ":" + this.port + "]  @ server [" + host + ":" + port + "]");
      Registry myRegistry = LocateRegistry.getRegistry(host, port);
      IServer master = (IServer) myRegistry.lookup(RMI_IDENTIFIER_MASTER);
      boolean registered = master.register(address, this.port);
      System.out.println("Finished to register Cluster Node [" + name + "@" + address + ":" + this.port + "]  @ server [" + host + ":" + port + "]: "
        + registered);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Node(String name, String address, int port, int threadPoolSize) throws RemoteException {
    super();

    if (address == null) {
      throw new NullPointerException("'address' must not be null");
    }
    if (address.trim().length() == 0) {
      throw new IllegalArgumentException("'address' must not be empty");
    }

    if (name == null) {
      throw new NullPointerException("'name' must not be null");
    }
    if (name.trim().length() == 0) {
      throw new IllegalArgumentException("'name' must not be empty");
    }

    if (port <= 0) {
      throw new IllegalArgumentException("'port' must not be less or equal than 0");
    }

    if (threadPoolSize <= 0) {
      throw new IllegalArgumentException("'threadPoolSize' must not be less or equal than 0");
    }

    System.getProperties().setProperty("java.rmi.server.hostname", address);

    this.name = name;
    this.address = address;
    this.port = port;

    try {
      this.repository = new Repository(getIdentifier());
    } catch (IOException e) {
      throw new RemoteException(e.getMessage());
    }

    this.handles = Executors.newFixedThreadPool(threadPoolSize, new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        return new Thread(r);
      }
    });
  }

  /** {@inheritDoc} */
  @Override
  public byte[] loadFile(String uri) throws RemoteException {
    if (uri == null) {
      Exception e = new NullPointerException("'uri' must not be null");
      throw new RemoteException(e.getMessage(), e);
    }
    if (uri.trim().length() == 0) {
      Exception e = new IllegalArgumentException("'uri' must not be empty");
      throw new RemoteException(e.getMessage(), e);
    }

    Future<byte[]> response = handles.submit(new Handle(uri));
    try {
      return response.get();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  private class Handle implements Callable<byte[]> {

    private String uri;

    public Handle(String uri) {
      if (uri == null) {
        throw new NullPointerException("'uri' must not be null");
      }
      if (uri.trim().length() == 0) {
        throw new IllegalArgumentException("'uri' must not be empty");
      }
      this.uri = uri;
    }

    /** {@inheritDoc} */
    @Override
    public byte[] call() throws Exception {
      return repository.get(Properties.WEBHOME + uri);
    }

  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      throw new NullPointerException("'obj' must not be null");
    }

    if (obj == this) {
      return true;
    }

    if (!(obj instanceof IClusterNode)) {
      return false;
    }

    IClusterNode n = (IClusterNode) obj;

    try {
      if (!n.getIdentifier().equals(getIdentifier())) {
        return false;
      }
    } catch (RemoteException e) {
      throw new IllegalStateException(e);
    }

    return true;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int result = 17;
    result = result + 17 + name.hashCode();
    result = result + 17 + address.hashCode();
    result = result + 17 + port;
    return result;
  }
}
