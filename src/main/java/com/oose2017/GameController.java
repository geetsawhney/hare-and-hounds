
package com.oose2017;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;

import static spark.Spark.*;

public class GameController {

	private static final String API_CONTEXT = "/hareandhounds/api";

	private final GamesCollection games;

	private final Logger logger = LoggerFactory.getLogger(GameController.class);

	public GameController(GamesCollection games) {
		this.games=games;
		setupEndpoints();
	}

	private void setupEndpoints() {

		// Start a game

		post(API_CONTEXT + "/games", "application/json", (request, response) -> {
				HashMap<String,String> outputMap;
				try{
					outputMap = games.addNewGame(request.body());
					response.status(201);
					return outputMap;
				} catch(IncorrectJsonException ex) {
					// Invalid pieceType
					response.status(400);
					return Collections.EMPTY_MAP;
				}
		},new JsonTransformer());

		//adds a player to an existing game
		put(API_CONTEXT + "/games/:gameId", "application/json", (request, response) -> {
			try {
				String gameId = request.params(":gameId"); //find the game to add a player to
				HashMap<String, String> returnMap = this.games.addPlayer(gameId);
				response.status(200);
				return returnMap;
			} catch (NoSpaceException ex) {
				logger.error(ex.getMessage());
				response.status(410);
				return ex.getHash();
			} catch (InvalidGameException ex) {
				logger.error(ex.getMessage());
				response.status(404);
				return ex.getHash();
			} catch (IncorrectJsonException ex) {
				logger.error(ex.getMessage());
				response.status(400);
				return ex.getHash();
			}
		}, new JsonTransformer());

		// a move made in the game post
		post(API_CONTEXT + "/games/:gameId/turns", "application/json", (request, response) -> {
			try {
				String gameId = request.params(":gameId");
				HashMap<String, String> outputMap = games.move(request.body(), gameId);
				response.status(200);
				return outputMap;
			} catch (InvalidGameException ex) {
				logger.error(ex.getMessage());
				response.status(404);
				return ex.getHash();
			} catch (InvalidPlayerException ex) {
				logger.error(ex.getMessage());
				response.status(404);
				return ex.getHash();
			} catch (IncorrectTurnException ex) {
				logger.error(ex.getMessage());
				response.status(422);
				return ex.getHash();
			} catch (IllegalMoveException ex) {
				logger.error(ex.getMessage());
				response.status(422);
				return ex.getHash();
			} catch (IncorrectJsonException ex) {
				logger.error(ex.getMessage());
				response.status(400);
				return ex.getHash();
			}
		}, new JsonTransformer());


		//describe the game state
		get(API_CONTEXT + "/games/:gameId/state", "application/json", (request, response) -> {
			try {
				String gameId = request.params(":gameId");
				HashMap<String, String> outputMap = games.fetchState(gameId);
				response.status(200);
				return outputMap;
			} catch (InvalidGameException ex) {
				logger.error(ex.getMessage());
				response.status(404);
				return ex.getHash();
			} catch (IncorrectJsonException ex) {
				logger.error(ex.getMessage());
				response.status(400);
				return ex.getHash();
			}
		}, new JsonTransformer());

			//retrieves the board states
		get(API_CONTEXT + "/games/:gameId/board", "application/json", (request, response) -> {
			try {
				response.status(200);
				String gameId=request.params(":gameId");
				return games.fetchBoard(gameId);

			}
 			catch (InvalidGameException ex) {
				logger.error(ex.getMessage());
				response.status(404);
				return ex.getHash();
			} catch (IncorrectJsonException ex) {
				logger.error(ex.getMessage());
				response.status(400);
				return ex.getHash();
			}
		}, new JsonTransformer());
	}
}