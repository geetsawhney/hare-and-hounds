package com.oose2017;

import com.google.gson.Gson;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*this class will hold all the games that are created in a List*/
public class GamesCollection {

    private ArrayList<Game> allGames;   //list of all games
    private int numberOfGames;          //total number of games
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(GamesCollection.class);



    /**initialising the collection of all games

     **/
    public GamesCollection(){
        allGames =new ArrayList<>();
        numberOfGames=0;
    }

    /**getter method for list of all games**/
    public ArrayList<Game> getAllGames() {
        return allGames;
    }

    /**getter method for size all games in the list**/
    public int getNumberOfGames() {
        return numberOfGames;
    }

    /**method to add new game using the ppiecetype received from the request bod
     * @throws IncorrectJsonException for sending BAD json in the body
    **/
    public HashMap<String,String> addNewGame(String body) throws IncorrectJsonException {
        HashMap <String,String> returnMap=new HashMap<>();
        if(body.isEmpty())
            throw  new IncorrectJsonException("Body is Empty");

        //Deserialize using GameInformationStart class
        GameInformationStart startInfo= new Gson().fromJson(body,GameInformationStart.class);
        String pieceType=startInfo.getPieceType();

        /**if no piece legal piece obtained from the @param body
        **/
         if(!(pieceType.equals("HARE") || pieceType.equals("HOUND")))
            throw new IncorrectJsonException("Illegal_JSON");
        //create a new game and add it to the list of games
        int gameId=numberOfGames;
        Game newGame=new Game(gameId,pieceType);
        allGames.add(newGame);
        numberOfGames++;
        //return map in to the GamesController class in a proper format
        returnMap.put("gameId",String.valueOf(gameId));
        returnMap.put("playerId",newGame.getPlayer1().getPlayerId());
        returnMap.put("pieceType",pieceType);
        return returnMap;
    }

    //to add a second player to a game which has already been created.
    public HashMap<String,String> addPlayer(String gameId) throws NoSpaceException, InvalidGameException, IncorrectJsonException {

        //check if
        if(gameId==null){
            throw new InvalidGameException("Incorrect Game ID and bad JSon");
        }

        int gameIdToJoin;
        try{
            gameIdToJoin=Integer.parseInt(gameId);
        }catch (Exception e){
            throw new InvalidGameException("Invalid Game ID");
        }

        if(gameIdToJoin>=allGames.size())
            throw  new InvalidGameException("Invalid Game");

        HashMap<String,String> returnMap=new HashMap<>();
        Game gameJoin= allGames.get(gameIdToJoin);
        if(gameJoin.getPlayer2()!=null){
            throw new NoSpaceException("NO SPACE FOR ANOTHER PLAYER");
        }

        gameJoin.addPlayer2(getOtherPlayerPieceType(gameJoin.getPlayer1().getPieceType()));
        returnMap.put("gameId",gameJoin.getGameId());
        returnMap.put("playerId",gameJoin.getPlayer2().getPlayerId());
        returnMap.put("pieceType",gameJoin.getPlayer2().getPieceType());

        return returnMap;
    }

    //make a move on the move and return the map of playerId
    public HashMap<String,String> move(String body, String gameId) throws InvalidPlayerException,
                IllegalMoveException, IncorrectTurnException, InvalidGameException, IncorrectJsonException {
            if(gameId==null){
                throw new InvalidGameException("Incorrect Game ID and bad JSon");
            }
            int gameIdToJoin;
            try{
                gameIdToJoin=Integer.parseInt(gameId);
            }catch (Exception e){
                throw new InvalidGameException("Invalid Game ID");
            }
            if(gameIdToJoin>=getNumberOfGames())
                throw  new InvalidGameException("Invalid Game");

            if(body.isEmpty())
                throw new IncorrectJsonException("Bad JSON");
            Turn turnTaken=new Gson().fromJson(body,Turn.class);

            if(!turnTaken.playerId.equals("Player1") && !turnTaken.playerId.equals("Player2"))
                throw new InvalidPlayerException("Wrong Player ID");

            Game gameMove= allGames.get(gameIdToJoin);
            return gameMove.makeMove(turnTaken);
    }

    /**get the state of the given gameId
     * return the map of state as required**/

    public HashMap<String, String> fetchState(String gameId) throws IncorrectJsonException, InvalidGameException {
        if(gameId==null){
            throw new InvalidGameException("Incorrect GameID");
        }
        int gameIdToJoin;
        try{
           gameIdToJoin = Integer.parseInt(gameId);
        }catch (Exception ex){
            throw new InvalidGameException("GAME ID is in bad format");
        }

        if(gameIdToJoin>=getNumberOfGames()){
            throw  new InvalidGameException("Invalid GameID, GameID is wrong");
        }

        Game gameToBeUsed=allGames.get(gameIdToJoin);
        HashMap<String,String> returnMap=new HashMap<>();
        returnMap.put("state",gameToBeUsed.getGameState());
        return returnMap;
    }

    //to get the description of the board
    public List<HashMap<String,String>> fetchBoard(String gameId) throws IncorrectJsonException, InvalidGameException {
        //check if the gameId is invalid and throw an exception
        if(gameId==null){
            throw new InvalidGameException("Incorrect GameID");
        }
        int gameIdToJoin;
        try{
            gameIdToJoin = Integer.parseInt(gameId);
        }catch (Exception ex){
            throw new InvalidGameException("GAME ID is in bad format");
        }
        if(gameIdToJoin>=getNumberOfGames()){
            throw  new InvalidGameException("Invalid GameID, GameID is wrong");
        }
        Game gameToBeUsed=allGames.get(gameIdToJoin);
        return gameToBeUsed.getGameBoardDescription();
    }

    //to input a pieceType and get the pieceType of the other Player
    private String getOtherPlayerPieceType(String pieceType) {
        if(pieceType.equals("HOUND"))
            return "HARE";
        return "HOUND";
    }
    private class GameInformationStart {
        private String pieceType;
        /**
         * a function to use to return the piece type
         * @return a string of the piece type
         */
        public String getPieceType() {
            return this.pieceType;
        }
    }
}