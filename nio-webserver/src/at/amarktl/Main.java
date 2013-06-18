
package at.amarktl;

import at.amarktl.cluster.IClusterNode;
import at.amarktl.cluster.Node;
import at.amarktl.cluster.Server;

public class Main {

  public static void main(String[] args) throws Exception {

    int portRMI = 1099;
    String serverAddress = "127.0.0.1";

    Server s = new Server("10.110.20.212", portRMI, 8080, 10);
    s.start();

    IClusterNode n1 = new Node("N1", "10.110.20.212", 2099, 5);
    n1.connect(serverAddress, portRMI);

    IClusterNode n2 = new Node("N2", "10.110.20.212", 2199, 5);
    n2.connect(serverAddress, portRMI);

    IClusterNode n3 = new Node("N3", "10.110.20.212", 2299, 5);
    n3.connect(serverAddress, portRMI);

    IClusterNode n4 = new Node("N4", "10.110.20.212", 2399, 5);
    n4.connect(serverAddress, portRMI);

  }

}

//---------------------------- Revision History ----------------------------
//$Log$
//
