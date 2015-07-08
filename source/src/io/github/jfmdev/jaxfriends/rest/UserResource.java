package io.github.jfmdev.jaxfriends.rest;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import io.github.jfmdev.jaxfriends.dal.DbUtils;
import io.github.jfmdev.jaxfriends.dal.Group;
import io.github.jfmdev.jaxfriends.dal.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.pmw.tinylog.Logger;

@Path("/users")
public class UserResource {
    @GET
    @Produces("application/json")
    public String list(@Context HttpServletRequest request) {
        String res;
        try{
            // Verify if the user is logged as administrator.
            if(RestUtils.isAdmin(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<User,String> usersDao = DaoManager.createDao(conn, User.class); 
                
                // List and return all groups from the user.
                List<User> users = usersDao.queryForAll();
                res = RestUtils.successResult(users);
                conn.close();
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged as administrator to get the list of users");
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            res = RestUtils.errorResult(e);
        }
        return res;
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public String findById(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        String res;
        try{
            // Verify if the user is logged.
            if(RestUtils.isAdmin(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<User,String> usersDao = DaoManager.createDao(conn, User.class); 
                
                // Search the user.
               User user = usersDao.queryForId(id+"");
               res = RestUtils.successResult(user);
               conn.close();
            } else {
                res = RestUtils.errorResult("AccessDenied", "You must be logged as administrator to get an user's data");
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            res = RestUtils.errorResult(e);
        }
        return res;
    }

    @POST
    @Produces("application/json")
    public String create(@Context HttpServletRequest request, 
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("admin") Integer isAdmin) {
        String res;
        try {
            // Verify if the user is logged as administrator.
            if(RestUtils.isAdmin(request)) {
                // Verify that neither the name or the password are empty.
                if(username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                    // Get connection to the database.
                    ConnectionSource conn = DbUtils.getConnection();
                    Dao<User,String> usersDao = DaoManager.createDao(conn, User.class); 

                    // Verify that the name is not already being used.
                    final Integer userId = RestUtils.getUserId(request);
                    List<User> users = usersDao.queryForEq("username", username);
                    if(users.isEmpty()) {
                        // Create the user.
                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(DbUtils.toSHA1(password));
                        user.setAdmin(isAdmin == 1);
                        usersDao.create(user);
    
                        // Set sucess response.
                        res = RestUtils.successResult();
                    } else {
                        // Set error message.
                        res = RestUtils.errorResult("InvalidParameters", "The username is already being used in other user");
                    }
                    
                    // Close connection to the database.
                    conn.close();
                } else {
                    // Set error message.
                    res = RestUtils.errorResult("InvalidParameters", "The username and the password can't be empty");
                }
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged as administrator to create an user");
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return res;
    }

    @PUT
    @Path("{id}")
    @Produces("application/json")
    public String update(@Context HttpServletRequest request, 
            @PathParam("id") Integer id,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("admin") Integer isAdmin ) {
        String res;
        try {
            // Verify if the user is logged as administrator.
            if(RestUtils.isAdmin(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<User,String> usersDao = DaoManager.createDao(conn, User.class); 

                // Verify if the user exists.
                User user = usersDao.queryForId(id+"");
                if(user != null) {
                    // Verify that the name is not already being used.
                    List<User> users = usersDao.queryForEq("username", username);
                    if(users.isEmpty() || Objects.equals(users.get(0).getId(), id)) {
                        // Update the user.
                        user.setUsername(username);
                        if(password != null && !password.isEmpty()) user.setPassword(DbUtils.toSHA1(password));
                        user.setAdmin(isAdmin == 1);
                        usersDao.update(user);
    
                        // Set sucess response.
                        res = RestUtils.successResult();
                    } else {
                        // Set error message.
                        res = RestUtils.errorResult("InvalidParameters", "The username is already being used in other user");
                    }                
                } else {
                    // Set error message.
                    res = RestUtils.errorResult("InvalidParameters", "The user do not exists");
                }
                    
                // Close connection to the database.
                conn.close();
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged as administrator to update an user");
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return res;
    }

    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public String delete(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        String res;
        try {
            // Verify if the user is logged.
            if(RestUtils.isAdmin(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<User,String> usersDao = DaoManager.createDao(conn, User.class); 
                Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 

                // Verify that the group is not used in any group.
                List<Group> groups = groupsDao.queryForEq("userId", id);
                if(groups.isEmpty()) {
                    // Delete the group.
                    usersDao.deleteById(id+"");

                    // Return response.
                    res = RestUtils.successResult();
                } else {
                    // Return error message.
                    res = RestUtils.errorResult("ReferentialConstraint", "You can't delete an user that is being used in some groups");
                }

                // Close connection to the database.
                conn.close();
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged as administrator to delete a group");
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return res;
    }
}
