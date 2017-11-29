package router;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import cpsc441.a4.shared.*;

/**
 * Router Class
 * 
 * This class implements the functionality of a router
 * when running the distance vector routing algorithm.
 * 
 * The operation of the router is as follows:
 * 1. send/receive HELLO message
 * 2. while (!QUIT)
 *      receive ROUTE messages
 *      update mincost/nexthop/etc
 * 3. Cleanup and return
 * 
 *      
 * @author 	Majid Ghaderi
 * 
 * finished by Jordan Nordh
 * 
 * @version	3.0
 *
 */
public class Router {
	
	/**
	 * Socket used to connect 
	 */
	private Socket socket;
	
	private ObjectOutputStream oos;
	
	private ObjectInputStream ois;
	
	/**
	 * linkcost[i] is the cost of link to router i
	 */
	int [ ] linkcost;
	
	/**
	 * nexthop[i] is the next hop node to reach router i
	 */
	int [ ] nexthop;
	
	/**
	 * mincost[i] is the mincost vector of router i
	 */
	int [] [] mincost;
	
	/**
	 * Update interval
	 */
	private int update;
	
	/**
	 * Router ID
	 */
	private int id;
	
	private boolean timerExpire;
	
	private Timer timer;

	private int numRouters;
	
    /**
     * Constructor to initialize the router instance 
     * 
     * @param routerId			Unique ID of the router starting at 0
     * @param serverName		Name of the host running the network server
     * @param serverPort		TCP port number of the network server
     * @param updateInterval	Time interval for sending routing updates to neighboring routers (in milli-seconds)
     */
	public Router(int routerId, String serverName, int serverPort, int updateInterval) {
		// set arrays to nothing
		linkcost = new int[0];
		nexthop = new int[0];
		mincost = new int[0][0];
		
		id = routerId;
		update = updateInterval;
		try {
			//Get the address by name
			InetAddress a = InetAddress.getByName(serverName);
			
			//Get a socket connecting to the server
			socket = new Socket(a, serverPort);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

    /**
     * starts the router 
     * 
     * @return The forwarding table of the router
     */
	public RtnTable start() {	
		System.out.println("Router " + id + " has started");
		// send HELLO to server
		DvrPacket p = new DvrPacket(this.id, DvrPacket.SERVER, DvrPacket.HELLO);
		timer = new Timer();
		try {
			// send packet to server
			oos.writeObject(p);
			oos.flush();
			System.out.println("Router " + id + " sending hello");
			p = (DvrPacket) ois.readObject();
			if (p.type != DvrPacket.HELLO){
				// Something bad happened
				System.exit(1);
			}
			
			System.out.println("Router " + id + " received hello");
			//get link cost from hello packet
			this.linkcost = p.getMinCost();
			this.numRouters = linkcost.length;
			
			this.initializeArrays();
			
			
			//Start timer
			timer.schedule(new Task(this), this.update);
			
			
			//running while loop
			
			p = (DvrPacket) ois.readObject();
			while ( p.type != DvrPacket.QUIT){
				System.out.println("Router " + id + " looping");
				//process the packet
				this.processDvr(p);
				
				//check for timer expire 
				if (this.timerExpire){
					//broadcast new link cost
					this.broadcast();
					
					//make a new timer
					timer.schedule(new Task(this), this.update);
					
					this.timerExpire = false;
				}
				
				// get another packet
				p = (DvrPacket) ois.readObject();
			}
			
			//Clean up
			oos.close();
			ois.close();
			socket.close();
			
			// cancel timer
			timer.cancel();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			//Clean up
			
			try {
				oos.close();
				ois.close();
				socket.close();
			} catch (Exception e) {}
			
			
			// cancel timer
			timer.cancel();
		}
		
		
		
		return new RtnTable(mincost[this.id], nexthop);
	}

/**
 * Initialize the arrays
 */
	private void initializeArrays() {
		//set other arrays to proper size
		nexthop = new int[numRouters];
		mincost = new int[numRouters][numRouters];
		
		for (int i = 0; i < this.numRouters; i++){
			nexthop[i] = -1;
			for (int j = 0; j < mincost[i].length; j++){
				mincost[i][j] = 999;
			}
		}
	}


	/**
	 * Sends this routers mincost to all connected routers
	 */
	private void broadcast() {
		// send this routers local mincost to all other routers
		
		DvrPacket p = null;
		timer = new Timer();
		try {
			for (int i = 0; i < this.linkcost.length; i++){
				if (i != this.id){
					p = new DvrPacket(this.id, i, DvrPacket.ROUTE);
					p.setMinCost(this.mincost[this.id]);
					oos.writeObject(p);
					oos.flush();
				}
			}			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Process a packet
	 * @param dvr Packet
	 */
	private void processDvr(DvrPacket dvr){
		//TODO
		if (dvr.sourceid == DvrPacket.SERVER){
			this.linkcost = dvr.getMinCost();
			this.broadcast();
			this.updateMinCost();
		}
		else {
			//update min cost vector
			this.mincost[dvr.sourceid] = dvr.getMinCost();
			this.updateMinCost();
		}
	}
	
	private void updateMinCost() {
		// Use bellman ford algorithm
		this.mincost[this.id][this.id] = 0;
		
		//set the min values
		for (int i = 0; i < linkcost.length; i++){
			
			int min = 999;
			int next = nexthop[i];
			
			for (int j = 0; j < mincost[i].length; j++){
				if (min > mincost[i][j]) min = mincost[i][j];
			}
			
			mincost[id][i] += min;
			
		}
		
		
		
	}


	/**
	 * Tell the router the timer expired
	 */
	public void expire(){
		this.timerExpire = true;
	}
	
    /**
     * A simple test driver
     * 
     */
	public static void main(String[] args) {
		
		// default parameters
		int routerId = 0;
		String serverName = "localhost";
		int serverPort = 8887;
		int updateInterval = 1000; //milli-seconds
		
		if (args.length == 4) {
			routerId = Integer.parseInt(args[0]);
			serverName = args[1];
			serverPort = Integer.parseInt(args[2]);
			updateInterval = Integer.parseInt(args[3]);
		} else {
			System.out.println("incorrect usage, try again.");
			//System.exit(0);
			//TODO remove comment
		}
			
		// print the parameters
		System.out.printf("starting Router #%d with parameters:\n", routerId);
		System.out.printf("Relay server host name: %s\n", serverName);
		System.out.printf("Relay server port number: %d\n", serverPort);
		System.out.printf("Routing update intwerval: %d (milli-seconds)\n", updateInterval);
		
		// start the router
		// the start() method blocks until the router receives a QUIT message
		Router router = new Router(routerId, serverName, serverPort, updateInterval);
		
		RtnTable rtn = router.start();
		System.out.println("Router terminated normally");
		
		// print the computed routing table
		System.out.println();
		System.out.println("Routing Table at Router #" + routerId);
		System.out.print(rtn.toString());
	}

}
