
/**
 * @author: Brad Fitzgerald, Student-ID 100969645 
 * @version 1.0
 * @date: November 2 2018
 * @title NET 4005 Assignment 2: RMI Client Server Interaction 
 */

import java.rmi.*;

/**
 * @class rsvclient: The reservation client interacts with user and sends
 *        requests to the server based on user input. User input is taken as
 *        command line parameters, output is printed on the screen as text. In
 *        particular, the client may send the following requests to the server.
 */
public class rsvclient {

	public static void main(String[] args) {
		try {

			/*
			 * Useful code that can be used to demonstrate the reserve function of client
			 * class. It generates and fills the available seats (30 passengers). Change 
			 * localhost to whatever remote IP the server is using if required.
			 *
			 * for (int i=1; i<=30; i++) { resvdb client =
			 * (resvdb)Naming.lookup("//localhost/rsv"); if(i<=5) {
			 * System.out.println(client.reserve("business", "Person-"+i, i)); } else {
			 * System.out.println(client.reserve("economy", "Person-"+i, i)); } }
			 */
			 
			System.out.println(); //print a line before program output
			// Complete the list operation when the user has entered the args variables:
			// list <Server Address>
			if (args[0].equals("list") && args.length == 2) {
				resvdb client = (resvdb) Naming.lookup("rmi://" + args[1] + "/rsv");
				System.out.println(client.list());
				// Complete the passengerlist operation when the user has entered the args
				// variables: passengerlist <Server Address>
			} else if (args[0].equals("passengerlist") && args.length == 2) {
				resvdb client = (resvdb) Naming.lookup("rmi://" + args[1] + "/rsv");
				System.out.println(client.getpassengers());
				// Complete the reserve operation when the user has entered the args variables:
				// reserve <server_name> <class> <passenger_name> <seat_number>
			} else if (args[0].equals("reserve") && (args[2].equals("business") || args[2].equals("economy"))
					&& args.length == 5) {
				resvdb client = (resvdb) Naming.lookup("rmi://" + args[1] + "/rsv");
				String arg4 = args[4];
				int seatNum = Integer.parseInt(arg4);
				System.out.println(client.reserve(args[2], args[3], seatNum));
				// Print usage if incorrect input detected
			} else {
				// Argument error handling
				System.out.print("Error!\nIncorrect Argument Provided!\n\nUsage:\n"
						+ " rsvclient reserve <server_name> <class:business or economy> <passenger_name> <seat_number> \n "
						+ "rsvclient passengerlist <server_name>\n rsvclient list <server_name>\n");
			}

			// Error handling
		} catch (Exception e) {

			String ex = e.getClass().toString();
			if (ex.equals("class java.rmi.ConnectException")) {
				System.out.println("Error!\nLooks like " + args[1] + " isn't reachable."
						+ "\nIs rmiregistry running on the server side?\n");
			} else if (ex.equals("class java.lang.ArrayIndexOutOfBoundsException")) {
				System.out.println("Error!\nPlease provide an argument!\n");
			} else {

				System.out.println(e.toString());
			}

			System.out.print("Usage:\n"
					+ " rsvclient reserve <server_name> <class:business or economy> <passenger_name> <seat_number> \n "
					+ "rsvclient passengerlist <server_name> \n rsvclient list <server_name>\n");
		}
	}
}