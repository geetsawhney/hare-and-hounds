package com.oose2017;

public class Player {
    private String playerId;
    private String pieceType;

    public Player(String playerId, String pieceType) {
        this.playerId=playerId;
        this.pieceType=pieceType;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPieceType() {
        return pieceType;
    }
}
