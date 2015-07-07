package io.github.jfmdev.jaxfriends.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.jfmdev.jaxfriends.dal.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        res.add("data", data != null? gson.toJsonTree(data) : null);
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

    /**
     * Returns the id of the user, is available.
     * 
     * @param request The Request's object.
     * @return The user's id or 'null' if the user is not logged.
     */
    public static Integer getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
    	Object tmpUser = session.getAttribute("user");
        if(tmpUser != null && tmpUser instanceof User) {
            User user = (User) tmpUser;
            return user.getId();
        }
        return null;
    }
    
    /**
     * Verifies if the user is logged.
     * 
     * @param request The Request's object.
     * @return 'true' if the user is logged, 'false' otherwise.
     */
    public static boolean isLogged(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
    	return session.getAttribute("user") != null;
    }
    
    /**
     * Verifies if the user is logged and is an administrator.
     * 
     * @param request The Request's object.
     * @return 'true' if the user is logged and is an administrator, 'false' otherwise.
     */
    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
    	Object tmpUser = session.getAttribute("user");
        if(tmpUser != null && tmpUser instanceof User) {
            User user = (User) tmpUser;
            return user.getAdmin() == true;
        }
        return false;
    }
}
