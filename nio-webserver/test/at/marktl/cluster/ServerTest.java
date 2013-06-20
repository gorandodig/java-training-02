
/** 
 * Copyright 2013 SSI Schaefer PEEM GmbH. All Rights reserved. 
 * <br /> <br />
 * 
 * $Id$
 * <br /> <br />
 *
 */

package at.marktl.cluster;

import static org.powermock.api.mockito.PowerMockito.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import at.amarktl.cluster.Server;



/**
 * This is the class header. The first sentence (ending with "."+SPACE) is important,
 * because it is used summary in the package overview pages.<br />
 * <br />
 *
 *
 * @author  ara
 * @version $Revision$
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServerSocketChannel.class)
public class ServerTest {

  @BeforeClass
  public static void beforClass(){
    mockStatic(ServerSocketChannel.class);
  }
  
  @Before
  public void setUp() throws Exception {
    
    ServerSocketChannel sscMock = mock(ServerSocketChannel.class);
    suppress(method(ServerSocketChannel.class, "open"));

    when(ServerSocketChannel.open()).thenReturn(sscMock);


  }

  @Test
  public void test() throws IOException {

    InetSocketAddress address = new InetSocketAddress(1);
    Server server = new Server("127.0.0.1", 1099, 8080, 10);
    server.start();
    


  }

}


//---------------------------- Revision History ----------------------------
//$Log$
//
