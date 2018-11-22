# RMI-Flight-Reservation-Server

﻿
██████╗ ███████╗ █████╗ ██████╗ ███╗   ███╗███████╗████████╗██╗  ██╗████████╗
██╔══██╗██╔════╝██╔══██╗██╔══██╗████╗ ████║██╔════╝╚══██╔══╝╚██╗██╔╝╚══██╔══╝
██████╔╝█████╗  ███████║██║  ██║██╔████╔██║█████╗     ██║    ╚███╔╝    ██║   
██╔══██╗██╔══╝  ██╔══██║██║  ██║██║╚██╔╝██║██╔══╝     ██║    ██╔██╗    ██║   
██║  ██║███████╗██║  ██║██████╔╝██║ ╚═╝ ██║███████╗██╗██║   ██╔╝ ██╗   ██║   
╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═════╝ ╚═╝     ╚═╝╚══════╝╚═╝╚═╝   ╚═╝  ╚═╝   ╚═╝   

Description->
A simple air-ticket reservation system using an RMI client-server implementation. 

****Files Included****
rsvserver.java - Server Program
rsvclient.java - Client Program
resvdb.java - Interface for rsvserver methods
README.txt - This document
 

****Usage:****
Client:
java rsvclient reserve <server_name> <class:business or economy> <passenger_name> <seat_number>
java rsvclient passengerlist <server_name>
java rsvclient list <server_name>

Example:
java rsvclient reserve localhost business Brad 1
java rsvclient list localhost
java rsvclient passengerlist localhost

Server:
start rmiregistry (windows) or rmiregistry & (linux)
java rsvserver
