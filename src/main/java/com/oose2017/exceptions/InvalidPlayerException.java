package com.oose2017.exceptions;

import java.util.HashMap;

public class InvalidPlayerException extends Exception {
    /** the response hash map*/
    private HashMap<String, String> response;
    /**
     * a constructor for the invalid player exception
     * @param message the context
     */
    public InvalidPlayerException(String message) {
        super(message);
        this.response = new HashMap<>();
        this.response.put("reason", "INVALID_PLAYER_ID");
    }
    /**
     * a getter for the formatted response to be transformed to json
     * @return the response
     */
    public HashMap<String, String> getHash() {
        return this.response;
    }
}
