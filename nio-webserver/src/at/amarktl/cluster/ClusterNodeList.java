
package at.amarktl.cluster;

import java.util.ArrayList;
import java.util.List;

class ClusterNodeList {

  private List<IClusterNode> nodes = new ArrayList<IClusterNode>();
  private int idx = 0;

  public synchronized void add(IClusterNode node) {
    if (node == null) {
      throw new NullPointerException("'node' must not be null");
    }
    nodes.add(node);
  }

  public synchronized IClusterNode next() {
    idx++;
    if (idx == nodes.size()) {
      idx = 0;
    }
    return nodes.get(idx);
  }

  public boolean isEmpty() {
    return nodes.isEmpty();
  }

}
