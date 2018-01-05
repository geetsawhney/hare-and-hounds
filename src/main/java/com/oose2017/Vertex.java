package com.oose2017;

public class Vertex {
    private final int x;
    private final int y;
    private String pieceType;

    public Vertex(int x, int y, String pieceType){
        this.x=x;
        this.y=y;
        this.pieceType=pieceType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPieceType(String pieceType){
        this.pieceType=pieceType;
    }

    public String getPieceType() {
        return pieceType;
    }
}