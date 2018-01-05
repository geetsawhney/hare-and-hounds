package com.oose2017;

import java.util.HashMap;

public class NoSpaceException extends Exception{
    HashMap<String,String> response;
    public NoSpaceException(String message){
        super(message);
        response=new HashMap<>();
        response.put("reason","NO_SPACE_FOUND");
    }
    public HashMap<String,String> getHash() {
        return response;
    }
}
