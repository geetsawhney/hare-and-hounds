package com.oose2017;


import com.oose2017.exceptions.IncorrectTurnException;

import java.util.*;

public class Board {

    private List<Vertex> allVertices;

    public Board() {
        allVertices=new ArrayList<>();
        allVertices.add(new Vertex(1,0, "HOUND"));
        allVertices.add(new Vertex(2,0, "NULL"));
        allVertices.add(new Vertex(3,0, "NULL"));
        allVertices.add(new Vertex(0,1, "HOUND"));
        allVertices.add(new Vertex(1,1, "NULL"));
        allVertices.add(new Vertex(2,1, "NULL"));
        allVertices.add(new Vertex(3,1, "NULL"));
        allVertices.add(new Vertex(4,1, "HARE"));
        allVertices.add(new Vertex(1,2, "HOUND"));
        allVertices.add(new Vertex(2,2, "NULL"));
        allVertices.add(new Vertex(3,2, "NULL"));
    }

    public String getPieceTypeByPosition(int x, int y) {
        for (Vertex vtx : this.allVertices) {
            if (vtx.getX() == x && vtx.getY() == y) {
                return vtx.getPieceType();
            }
        }
        return null;
    }

    public void setPieceTypeByPosition(int x, int y, String pieceType) throws IncorrectTurnException {
        if (pieceType.equals("HARE") || pieceType.equals("HOUND") || pieceType.equals("NULL")) {
            for (Vertex v : allVertices) {
                if (v.getX() == x && v.getY() == y) {
                    v.setPieceType(pieceType);
                }
            }
        } else {
            throw new IncorrectTurnException("Wrong Piece Type");
        }
    }

    public ArrayList<Vertex> getAllVerticesByPieceType(String pieceType) {
        ArrayList<Vertex> pieces = new ArrayList<>();
        for (Vertex v : allVertices) {
            if (v.getPieceType().equals(pieceType)) {
                pieces.add(v);
            }
        }
        return pieces;
    }

    public boolean checkHareEscaped() {
        Vertex hareVertex = getAllVerticesByPieceType("HARE").get(0);
        ArrayList<Vertex> houndVertices = getAllVerticesByPieceType("HOUND");
        for (Vertex v : houndVertices) {
            if (hareVertex.getX() > v.getX())
                return false;
        }
        return true;
    }

    public boolean checkHoundWin() {
        Vertex hareVertex = getAllVerticesByPieceType("HARE").get(0);
//      ArrayList<Vertex> houndVertecies = getAllVerticesByPieceType("HOUND");
        ArrayList<Vertex> nextPositions = getPossibleMoves(hareVertex);
        for (Vertex v : nextPositions) {
            if (v.getPieceType().equals("NULL"))
                return false;
        }
        return true;
    }
    private ArrayList<Vertex> getPossibleMoves(Vertex hareVertex) {
        ArrayList<Vertex> output = new ArrayList<>();
        for (Vertex v : allVertices) {
            if (isAdjacent(hareVertex, v)) {
                output.add(v);
            }
        }
        return output;
    }

    private boolean isAdjacent(Vertex v1, Vertex v2) {
        if (Math.max(Math.abs(v1.getX() - v2.getX()), Math.abs(v1.getY() - v2.getY())) != 1)
            return false;
        Set<String> isolatedVertices = new HashSet<String>(Arrays.asList("11_22_31_20".split("_")));
        if (isolatedVertices.contains("" + v1.getX() + v2.getX()) && isolatedVertices.contains("" + v1.getX() + v2.getX()))
            return false;
        return true;
    }

    public String getBoardStatus() {
        List<Vertex> houndVertices=getAllVerticesByPieceType("HOUND");
        Vertex hareVertex=getAllVerticesByPieceType("HARE").get(0);
        String output="";
        for(Vertex v:houndVertices){
            output+=v.getPieceType()+v.getX()+v.getY();
        }
        output=output+hareVertex.getPieceType()+hareVertex.getX()+hareVertex.getY();
        System.out.println(output);
        return  output;
    }
}