
package com.oose2017;

import com.oose2017.exceptions.IllegalMoveException;
import com.oose2017.exceptions.IncorrectTurnException;
import com.oose2017.exceptions.InvalidPlayerException;

import java.util.*;

import java.lang.Math;


public class Game {

	private int gameId;
	private GameState state;
	private Player player1;
	private Player player2;
	private Board board;
	private Boolean isFirstPlayerTurn;
	private int counter;
	private String currentTurn;
	HashMap<String,Integer> boardStatus;

	public Game(int gameId, String pieceType) {
		this.gameId=gameId;
		player1=new Player("Player1",pieceType);
		board=new Board();
		counter=0;
		state= GameState.valueOf("WAITING_FOR_SECOND_PLAYER");
	}

	public String getGameId() {
		return gameId+"";
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void addPlayer2(String pieceType) {
		player2=new Player("Player2",pieceType);
		if(player2.getPieceType().equals("HARE")) {
			isFirstPlayerTurn = true;
		}
		else isFirstPlayerTurn=false;
		currentTurn="HOUND";
		state= GameState.valueOf("TURN_HOUND");
		boardStatus=new HashMap<>();
		boardStatus.put(board.getBoardStatus(),1);
	}

	public HashMap<String,String> makeMove(Turn turnTaken) throws InvalidPlayerException,
            IllegalMoveException, IncorrectTurnException {

		int fromX=Integer.parseInt(turnTaken.fromX);
		int fromY=Integer.parseInt(turnTaken.fromY);
		int toX=Integer.parseInt(turnTaken.toX);
		int toY=Integer.parseInt(turnTaken.toY);

		if(fromX>4 ||fromY>2 ||toX>4|| toY>2 ||toX<0 ||fromX<0||toY<0||fromY<0)
			throw new IllegalMoveException("Invalid move type");

		String startPositionType=board.getPieceTypeByPosition(Integer.parseInt(turnTaken.fromX),
				Integer.parseInt(turnTaken.fromY));
		String endPositionType=board.getPieceTypeByPosition(Integer.parseInt(turnTaken.toX),
				Integer.parseInt(turnTaken.toY));
		String pieceTypePlayer=getPlayerPieceType(turnTaken.playerId);

		if(!pieceTypePlayer.equals(currentTurn)){
			throw new IncorrectTurnException("Incorrect Turn");
		}
		if(!pieceTypePlayer.equals("HARE") && !pieceTypePlayer.equals("HOUND")) {
			throw new InvalidPlayerException("Incorrect Player");
		}
		if(startPositionType.equals("NULL")){
			throw new IllegalMoveException("Illegal Move, can't move empty space");
		}
		if(!endPositionType.equals("NULL")) {
			throw new IllegalMoveException("Illegal Move, end position is not empty");
		}
		if(!checkMove(turnTaken,pieceTypePlayer)){
			throw new IllegalMoveException("Invalid Move");
		}
		if(!startPositionType.equals(currentTurn)){
			throw new IncorrectTurnException("Not Your Turn, it is "+state);
		}

		//legal moves
		board.setPieceTypeByPosition(Integer.parseInt(turnTaken.fromX),Integer.parseInt(turnTaken.fromY),"NULL");
		board.setPieceTypeByPosition(Integer.parseInt(turnTaken.toX),Integer.parseInt(turnTaken.toY),pieceTypePlayer);
		isFirstPlayerTurn=!isFirstPlayerTurn;
		if(currentTurn.equals("HOUND")) {
			state = GameState.valueOf("TURN_HARE");
			currentTurn = "HARE";
		}
		else {
			counter++;
			state = GameState.valueOf("TURN_HOUND");
			currentTurn="HOUND";
		}
		HashMap<String,String> returnMap= new HashMap<>();
		returnMap.put("playerId",turnTaken.playerId);
		if(checkGameOver()) {
		}
		return returnMap;
	}

	private boolean checkGameOver() {
		if (board.checkHareEscaped()) {
			state = GameState.valueOf("WIN_HARE_BY_ESCAPE");
			return true;
		}
		if (checkHareWinByStalling()) {
			state = GameState.valueOf("WIN_HARE_BY_STALLING");
			return true;
		}
		if (board.checkHoundWin()) {
			state = GameState.valueOf("WIN_HOUND");
			return true;
		}
		return false;
	}

	private boolean checkHareWinByStalling() {
		if(boardStatus.containsKey(board.getBoardStatus())){
			int value=boardStatus.get(board.getBoardStatus());
			if (value==2){
				return true;
			}
			boardStatus.put(board.getBoardStatus(),value+1);
		}else {
			boardStatus.put(board.getBoardStatus(),1);
		}
		return false;
	}

	private boolean checkMove(Turn turnTaken, String pieceType) throws IllegalMoveException {
		int fromX=Integer.parseInt(turnTaken.fromX);
		int fromY=Integer.parseInt(turnTaken.fromY);
		int toX=Integer.parseInt(turnTaken.toX);
		int toY=Integer.parseInt(turnTaken.toY);

		if(fromX>5 ||fromY>2 ||toX>5|| toY>2 ||toX<0 ||fromX<0||toY<0||fromY<0)
			throw new IllegalMoveException("Invalid move type");
		if(pieceType.equals("HOUND")){
			if(toX-fromX<0)
				throw new IllegalMoveException("Hound Cannot go back");
		}
		if(Math.max(Math.abs(fromX - toX), Math.abs(fromY - toY))!=1)
			throw new IllegalMoveException("Cannot jump more than one space");
		String[] inValidVertices={"00","02","40","42"};
		for(String s:inValidVertices) {
			if (s.equals(fromX + fromY + ""))
				throw new IllegalMoveException("invalid blocks");
			if (s.equals(toX + toY + "")) {
				throw new IllegalMoveException("invalid blocks");
			}
		}
		String[] badLocations={"11","20","22","31"};
		HashSet<String> isolatedVertices=new HashSet<>();
		for(String s:badLocations)
			isolatedVertices.add(s);
		if(isolatedVertices.contains(""+fromX+fromY) && isolatedVertices.contains(""+toX+toY)) {
			throw new IllegalMoveException("Unreachable node from current position");
		}
		return true;
	}

	private String getPlayerPieceType(String playerId){
		if(playerId.equals(player1.getPlayerId()))
			return player1.getPieceType();
		if (playerId.equals(player2.getPlayerId()))
			return player2.getPieceType();
		return "NULL";
	}

	public String getGameState() {
		return state.toString();
	}

	public List<HashMap<String,String>> getGameBoardDescription() {
		List<HashMap<String,String>> output=new ArrayList<>();
		ArrayList<Vertex> vertices=board.getAllVerticesByPieceType("HARE");
		vertices.addAll(board.getAllVerticesByPieceType("HOUND"));
		HashMap<String,String> map;
		for(Vertex v:vertices){
			map=new HashMap<>();
			map.put("pieceType",v.getPieceType());
			map.put("x", String.valueOf(v.getX()));
			map.put("y", String.valueOf(v.getY()));
			output.add(map);
		}
		return output;
	}
}