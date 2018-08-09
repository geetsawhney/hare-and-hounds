package com.oose2017.exceptions;

import java.util.HashMap;

public class IllegalMoveException extends Exception {
    /** the formatted response hash map.*/
    private HashMap<String, String> response;
    /**
     * the constructor for the exception
     * @param message the message with some context
     */
    public IllegalMoveException(String message) {
        super(message);
        this.response = new HashMap<>();
        this.response.put("reason", "ILLEGAL_MOVE");
    }
    /**
     * a getter for the hashmap
     * @return the hash map to be transformed to json
     */
    public HashMap<String, String> getHash() {
        return this.response;
    }
}