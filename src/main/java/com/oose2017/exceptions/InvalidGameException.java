package com.oose2017.exceptions;

import java.util.HashMap;

public class InvalidGameException extends Exception {
    /** the response.*/
    private HashMap<String, String> response;
    /**
     * the constructor for the exception that uses the super
     * @param message the context
     */
    public InvalidGameException(String message) {
        super(message);
        this.response = new HashMap<>();
        this.response.put("reason", "INVALID_GAME_ID");
    }
    /**
     * a getter for the formatted response to be transformed to json
     * @return the response
     */
    public HashMap<String, String> getHash() {
        return this.response;
    }
}
