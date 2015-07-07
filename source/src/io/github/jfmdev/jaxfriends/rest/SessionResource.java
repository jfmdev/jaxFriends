package io.github.jfmdev.jaxfriends.rest;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import io.github.jfmdev.jaxfriends.dal.DbUtils;
import io.github.jfmdev.jaxfriends.dal.User;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.pmw.tinylog.Logger;

@Path("/session")
public class SessionResource {
    @POST
    @Path("/login")
    @Produces("application/json")
    public String login(@Context HttpServletRequest request, 
            @FormParam("username") final String username, 
            @FormParam("password") final String password) {
        
        try{
            // Verify if the username and password are valid.
            ConnectionSource conn = DbUtils.getConnection();
            Dao<User,String> usersDao = DaoManager.createDao(conn, User.class); 
            List<User> users = usersDao.queryForFieldValues(new HashMap<String, Object>() {{
                put("username", username);
                put("password", DbUtils.toSHA1(password));
            }});
            conn.close();
            
            if(users.size() > 0) {
                // Save user data in the session.
                HttpSession session = request.getSession(true);
                session.setAttribute("user", users.get(0));
                
                // Return result.
                return RestUtils.successResult();
            } else {
                // The user entered an invalid combination of username and password.
                return RestUtils.errorResult("InvalidParameters", "Invalid username or password");
            }
            
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
    }

    @GET
    @Path("/logout")
    @Produces("application/json")
    public String logout(@Context HttpServletRequest request) {
        // Log out the user if it was logged.
        HttpSession session = request.getSession(true);
    	boolean logged = session.getAttribute("user") != null;
        if(logged) session.setAttribute("user", null);
        
        // Return result.
        return RestUtils.successResult();
    }

    @PUT
    @Path("/update")
    @Produces("application/json")
    public String udpate(@Context HttpServletRequest request, 
            @FormParam("username") String username, 
            @FormParam("password") String password) {
        String res;
        
        // Verify if the user is logged.
        HttpSession session = request.getSession(true);
    	Object data = session.getAttribute("user");
        
        if(data != null) {
            try {
                // Verify that the username if not empty.
                if(username != null && !username.isEmpty()) {
                    // Get connection to the database and current user.
                    ConnectionSource conn = DbUtils.getConnection();
                    Dao<User,String> usersDao = DaoManager.createDao(conn, User.class); 
                    User user = usersDao.queryForId( ((User)data).getId() + "" );

                    // Verify that the username is not already being used and close connection to the database.
                    List<User> users = usersDao.queryForEq("username", username);
                    if(users.isEmpty() || (users.size() == 1 && Objects.equals(users.get(0).getId(), user.getId()))) {
                        // Update the user.
                        user.setUsername(username);
                        if(password != null && password.length() > 0) user.setPassword(DbUtils.toSHA1(password));
                        usersDao.update(user);

                        // Return response.
                        res = RestUtils.successResult();
                    } else {
                        // Return error message.
                        res = RestUtils.errorResult("InvalidParameters", "The username is already being used by other user");
                    }
                    
                    // Close connection to the database.
                    conn.close();
                } else {
                    // Return error message.
                    res = RestUtils.errorResult("InvalidParameters", "The username can't be empty");
                }
            }catch(SQLException e) {
                // Log error and return error message.
                Logger.error(e);
                res = RestUtils.errorResult(e);
            }
        } else {
            // The user is not logged.
            res = RestUtils.errorResult("AccessDenied", "You must be logged to udpdate your account");
        }
        
        return res;
    }
}
