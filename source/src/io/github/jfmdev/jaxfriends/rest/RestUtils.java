package io.github.jfmdev.jaxfriends.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Utility class for REST operations.
 * 
 * @author jfmdev
 */
public class RestUtils {
    /**
     * Object to serialize/deserialize to/from JSON.
     */
    private static final Gson gson;
    
    /**
     * Static constructor.
     */
    static {
        gson = new Gson();
    }
    
    /**
     * Builds a basic result string representing a succesful result.
     * 
     * @return A JSON string.
     */
    public static String successResult() {
        return successResult(null);
    }
    
    /**
     * Builds a basic result string representing a succesful result.
     * 
     * @param data The data to append.
     * @return A JSON string.
     */
    public static String successResult(Object data) {
        JsonObject res = new JsonObject();
        res.addProperty("status", "success");
        res.addProperty("data", data != null? gson.toJson(data) : null);
        return gson.toJson(res);
    }
    
    /**
     * Builds a result string representing an exception.
     * 
     * @param e An exception.
     * @return  A JSON string with the error information.
     */
    public static String errorResult(Exception e) {
        return errorResult("Exception", e.getMessage());
    }
    
    /**
     * Builds a result string representing an error.
     * 
     * @param code The error code.
     * @param message The error message.
     * @return A JSON string with the error information.
     */
    public static String errorResult(String code, String message) {
        JsonObject res = new JsonObject();
        res.addProperty("status", "error");
        
        JsonObject error = new JsonObject();
        error.addProperty("code", code);
        error.addProperty("message", message);
        res.add("error", error);

        return gson.toJson(res);
    }
}
