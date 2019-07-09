package io.vertx.pagseguro.rmi;

import java.rmi.RemoteException;

/**
 * 
 * @author dbatista
 *
 */
public class HelloServiceImpl implements HelloService {

	@Override
	public String hello(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return "Hello " + name;
	}

}
