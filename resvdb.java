
/**
 * @author: Brad Fitzgerald, Student-ID 100969645 
 * @version 1.0
 * @date: November 2 2018
 * @title NET 4005 Assignment 2: RMI Client Server Interaction 
 */
import java.rmi.*;
/**
 * Interface for RMI server methods
 */
public interface resvdb extends java.rmi.Remote {
	public String reserve(String flyingClass, String name, int seatNum) throws RemoteException;
	public String getpassengers() throws RemoteException;
	public String list() throws RemoteException;	
}
