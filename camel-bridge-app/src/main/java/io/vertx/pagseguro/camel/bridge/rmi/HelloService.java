package io.vertx.pagseguro.camel.bridge.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * @author dbatista
 *
 */
public interface HelloService extends Remote {

	/**
	 * 
	 * @param name
	 * @return
	 * @throws RemoteException
	 */
	String hello(String name) throws RemoteException;
}
