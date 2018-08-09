//-------------------------------------------------------------------------------------------------------------//
// Code based on the ToDoApp from OOSE class
// https://github.com/jhu-oose/todo
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static spark.Spark.*;

public class Bootstrap {

	private static final String IP_ADDRESS = "localhost";
	private static final int PORT = 8080;
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);


	public static void main(String[] args) {

		ipAddress(IP_ADDRESS);
		port(PORT);
//		set the static file location
		staticFileLocation("/public");

		GamesCollection model = new GamesCollection();
		new GameController(model);
	}
}