
package test.java.testmult;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;

/**
 * Demonstrate asynchronous connection of a SocketChannel.
 *
 * Created: April 2002
 * @author Ron Hitchens (ron@ronsoft.com)
 * @version $Id: ConnectAsync.java,v 1.2 2002/04/28 01:47:58 ron Exp $
 */
public class ConnectAsync
{
	public static void main (String [] argv)
		throws Exception
	{
		String host = "localhost";
		int port = 1234;

		if (argv.length == 2) {
			host = argv [0];
			port = Integer.parseInt (argv [1]);
		}

		InetSocketAddress addr = new InetSocketAddress (host, port);
		SocketChannel sc = SocketChannel.open();

		sc.configureBlocking (false);

		System.out.println ("initiating connection");

		sc.connect (addr);

		while ( ! sc.finishConnect()) {
			doSomethingUseful(sc);
		}

		System.out.println ("connection established");

		// Do something with the connected socket
		// The SocketChannel is still non-blocking

		sc.close();
	}

	private static void doSomethingUseful(SocketChannel sc) throws IOException
	{
		ByteBuffer tmp = ByteBuffer.allocate(100);
		tmp.put(("hello word"+"\r\n").getBytes());
		tmp.flip();
		sc.write(tmp);
		
		System.out.println ("doing something useless");
	}
}
