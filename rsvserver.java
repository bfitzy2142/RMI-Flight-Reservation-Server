
/**
 * @author: Brad Fitzgerald, Student-ID 100969645 
 * @version 1.0
 * @date: November 2 2018
 * @title NET 4005 Assignment 2: RMI Client Server Interaction 
 */

import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.net.DatagramSocket;
import java.net.InetAddress;

@SuppressWarnings("serial")
/**
 * @class rsvserver: The reservation server (rsvserver) maintains the seat
 *        availability information and responds to client requests for
 *        reservation or availability.
 */
public class rsvserver extends UnicastRemoteObject implements resvdb {

	// rsvserver attributes
	private passenger[] passengerList;
	private int businessPrice;
	private int ecoPrice;

	// Default constructor
	rsvserver() throws RemoteException, UnknownHostException {
		passengerList = new passenger[30];
	}

	// reserve one seat for a passenger
	public String reserve(String flyingClass, String name, int seatNum) throws RemoteException {
		if (seatNum > 30 || seatNum < 0) {
			return "Failed to reserve: invalid seat number";
		} else if (seatNum < 6 && flyingClass.equals("economy") || seatNum >= 6 && flyingClass.equals("business")) {
			return "Failed to reserve: invalid seat number";
		} else if (!this.getAvailseats().contains(seatNum)) {
			return "Failed to reserve: seat not available";
		} else {
			if (seatNum < 6) {
				this.updatePrice(seatNum);
				passengerList[seatNum - 1] = new passenger("business", name, seatNum, businessPrice);
			} else {
				this.updatePrice(seatNum);
				passengerList[seatNum - 1] = new passenger("economic", name, seatNum, ecoPrice);
			}
		}
		return "Successfully reserved seat " + seatNum + " for passenger " + name + " at a price of "
				+ passengerList[seatNum - 1].getPrice();
	}

	// print the passengers in the system
	public String getpassengers() throws RemoteException {
		StringBuilder sb = new StringBuilder();
		Boolean found = false;

		for (int i = 0; i <= passengerList.length - 1; i++) {
			if (passengerList[i] != null) {
				found = true;
				sb.append(passengerList[i].getName() + " " + passengerList[i].getFlyingclass() + " "
						+ passengerList[i].getSeatnum() + "\n");
			}
		}

		if (found == false) {
			sb.append("No users booked yet!");
		}

		return sb.toString();
	}

	/*
	 * Provides a glance of the current available seats using a string builder
	 * 
	 * @returns String
	 */
	public String list() throws RemoteException {
		int businessSeatsavail500 = 3;
		int businessSeatsavail800 = 2;
		int ecoSeats200 = 10;
		int ecoSeats300 = 10;
		int lastEcoseats = 5;

		ArrayList<Integer> availableSeats = this.getAvailseats();
		ArrayList<Integer> businessFreeseats = new ArrayList<Integer>();
		ArrayList<Integer> ecoClass = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();

		// Determine business seat availability
		for (int i = 1; i <= 5; i++) {
			if (!availableSeats.contains(i) && businessSeatsavail500 > 0) { //if a seat is not within the arraylist, it is taken
				businessSeatsavail500--; 
			} else if (!availableSeats.contains(i) && businessSeatsavail500 == 0) {
				businessSeatsavail800--;
			}
		}

		// Determine economic seat availability
		for (int i = 6; i <= 30; i++) {
			if (!availableSeats.contains(i) && ecoSeats200 > 0) {
				ecoSeats200--;
			} else if (!availableSeats.contains(i) && ecoSeats300 > 0 && ecoSeats200 == 0) {
				ecoSeats300--;
			} else if (!availableSeats.contains(i) && ecoSeats200 == 0 && ecoSeats300 == 0) {
				lastEcoseats--;
			}
		}

		// add free seat values to their respective ArrayList
		for (int i = 1; i <= 30; i++) {
			if (availableSeats.contains(i) && i <= 5) {
				businessFreeseats.add(i);
			} else if (availableSeats.contains(i)) {
				ecoClass.add(i);
			}
		}

		// start building a string with data calculated
		sb.append("Business class:\n");
		sb.append(businessSeatsavail500 + " seats at $500 each\n");
		sb.append(businessSeatsavail800 + " seats at $800 each\n");
		sb.append("Seat numbers: ");
		for (int i = 0; i < businessFreeseats.size(); i++) {
			if (i != businessFreeseats.size() - 1) {
				sb.append(businessFreeseats.get(i) + ",");
			} else {
				sb.append(businessFreeseats.get(i));
			}
		}

		sb.append("\n" + "Economy class:\n");
		sb.append(ecoSeats200 + " seats at $200 each\n");
		sb.append(ecoSeats300 + " seats at $300 each\n");
		sb.append(lastEcoseats + " seats at $450 each\n");
		sb.append("Seat numbers: ");
		for (int i = 0; i < ecoClass.size(); i++) {
			if (i != ecoClass.size() - 1) {
				sb.append(ecoClass.get(i) + ",");
			} else {
				sb.append(ecoClass.get(i) + "\n");
			}
		}

		return sb.toString();
	}

	// get available seats using an Integer ArrayList
	private ArrayList<Integer> getAvailseats() {
		ArrayList<Integer> avail = new ArrayList<Integer>();

		for (int i = 0; i < 30; i++) {
			if (passengerList[i] == null) {
				avail.add(i + 1);
			}
		}
		return avail;
	}

	/*
	 * The updatePrice method will be called when a user reserves a seat. It will
	 * keep track of ticket prices based on seat availability.
	 */
	private void updatePrice(int desiredSeatNum) {
		int numpassengers = 0;

		// check if we need to update business seat price
		if (desiredSeatNum <= 5) {
			for (int i = 0; i < 5; i++) {
				if (passengerList[i] instanceof passenger) {
					numpassengers++;
				}
			}
			if (numpassengers >= 3) { // Price is 800 if three business seats are taken
				businessPrice = 800;
			} else {
				businessPrice = 500; // 500 otherwise
			}

		} else if (desiredSeatNum > 5) { // Update economic price when seat num > 5
			for (int i = 5; i < 30; i++) {
				if (passengerList[i] instanceof passenger) {
					numpassengers++;
				}
			}
			if (numpassengers < 10) {
				ecoPrice = 200;
			} else if (numpassengers >= 10 && numpassengers < 20) {
				ecoPrice = 300;
			} else if (numpassengers >= 20) {
				ecoPrice = 450;
			}
		}
	}
	// Main method of rsvserver class. Start the server @localhost and handle
	// errors.

	public static void main(String[] args) {
		try {
			String serverIP = "";
			try (final DatagramSocket socket = new DatagramSocket()) {
				socket.connect(InetAddress.getByName("10.0.0.0"), 80);
				serverIP = socket.getLocalAddress().getHostAddress();
				System.setProperty("java.rmi.server.hostname", serverIP);
			}
			rsvserver server = new rsvserver();
			Naming.rebind("rsv", server);
			System.out.println(
					"RMI Flight Reservation Server Started!\n" + "Server running on socket: " + serverIP + ":1099");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Make sure rmiregistry is running!");
		}
	}

}

/**
 * @class passenger: Used by the rsvserver class to keep track of individuals
 *        reserving seats. When the client creates a reservation, an instance of
 *        this class is created to store their name, flying class, seat number,
 *        and price of their ticket. Getter methods are used by the methods of
 *        rsvserver.
 */
class passenger {
	// passenger attributes
	private int seatNumber;
	private String name;
	private String flyingClass;
	private int ticketPrice;

	// constructor for the passenger class
	passenger(String flyingClass, String name, int seatNumber, int ticketPrice) {
		this.flyingClass = flyingClass;
		this.name = name;
		this.seatNumber = seatNumber;
		this.ticketPrice = ticketPrice;
	}

	// return an integer representing the passenger's seat number.
	public int getSeatnum() {
		return seatNumber;
	}

	// return an integer representing the passenger's seat price.
	public int getPrice() {
		return ticketPrice;
	}

	// return the name of the passenger as a String.
	public String getName() {
		return name;
	}

	// return the flying class of the passenger <business/economic>
	public String getFlyingclass() {
		return flyingClass;
	}

}