package com.oose2017;

import com.google.gson.Gson;
import spark.Response;
import spark.ResponseTransformer;

import java.util.HashMap;

public class JsonTransformer implements ResponseTransformer {
    /** the gson object that is transformed from the response*/
    private Gson gson = new Gson();

    /**
     * the render class for the gson object to appear as json
     */
    @Override
    public String render(Object model) {
        if (model instanceof Response) {
            return gson.toJson(new HashMap<>());
        }
        /**transforms the gson to json*/
        return gson.toJson(model);
    }

}
