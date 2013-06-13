
package at.amarktl.cluster;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {

  boolean register(String host, int port) throws RemoteException;

}

//---------------------------- Revision History ----------------------------
//$Log$
//
