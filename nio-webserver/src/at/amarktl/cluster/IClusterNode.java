
package at.amarktl.cluster;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Copyright 2013 SSI Schaefer PEEM GmbH. All Rights reserved. <br />
 * <br />
 * $Id$ <br />
 * <br />
 * TODO ADD/Complete JavaDoc! IClusterNode provides methods to TODO xxx. IClusterNode is (not)
 * immutable and may (not) be freely exchanged between Threads. Calls to methods of IClusterNode are
 * (not) thread safe. Usage: TODO add some usage examples.
 * 
 * @author marktl
 * @version $Revision$
 */

public interface IClusterNode extends Remote {

  String getIdentifier() throws RemoteException;

  byte[] loadFile(String uri) throws RemoteException;

  void connect(String server, int serverport) throws RemoteException;

}

//---------------------------- Revision History ----------------------------
//$Log$
//
