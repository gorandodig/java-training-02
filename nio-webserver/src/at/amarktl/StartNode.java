
package at.amarktl;

import at.amarktl.cluster.IClusterNode;
import at.amarktl.cluster.Node;

public class StartNode {

  public static void main(String[] args) throws Exception {

    int portRMI = 1099;
    String serverAddress = "10.110.20.212";

    IClusterNode n1 = new Node("N1", "10.110.20.212", 2099, 10);
    n1.connect(serverAddress, portRMI);

  }

}

//---------------------------- Revision History ----------------------------
//$Log$
//
