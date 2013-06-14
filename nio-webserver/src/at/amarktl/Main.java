
package at.amarktl;

import at.amarktl.cluster.IClusterNode;
import at.amarktl.cluster.Node;
import at.amarktl.cluster.Server;

public class Main {

  public static void main(String[] args) throws Exception {

    int portRMI = 1099;
    String serverAddress = "127.0.0.1";

    Server s = new Server("127.0.0.1", portRMI, 8080, 10);
    s.start();

    IClusterNode n1 = new Node("N1", "127.0.0.1", 2099, 10);
    n1.connect(serverAddress, portRMI);

    IClusterNode n2 = new Node("N2", "127.0.0.1", 2199, 10);
    n2.connect(serverAddress, portRMI);

    IClusterNode n3 = new Node("N3", "127.0.0.1", 2299, 10);
    n3.connect(serverAddress, portRMI);

  }

}

//---------------------------- Revision History ----------------------------
//$Log$
//
