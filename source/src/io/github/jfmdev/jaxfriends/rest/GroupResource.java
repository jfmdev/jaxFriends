package io.github.jfmdev.jaxfriends.rest;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import io.github.jfmdev.jaxfriends.dal.DbUtils;
import io.github.jfmdev.jaxfriends.dal.Friend;
import io.github.jfmdev.jaxfriends.dal.Group;
import java.sql.SQLException;
import java.util.HashMap;
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

@Path("/groups")
public class GroupResource {
    @GET
    @Produces("application/json")
    public String list(@Context HttpServletRequest request) {
        try{
            // Verify if the user is logged.
            if(RestUtils.isLogged(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                
                // List and return all groups from the user.
                List<Group> groups = groupsDao.queryForEq("userId", RestUtils.getUserId(request));
                return RestUtils.successResult(groups);
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return RestUtils.errorResult("AccessDenied", "You must be logged to get the list of groups");
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public String findById(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try{
            // Verify if the user is logged.
            if(RestUtils.isLogged(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                
                // List and return all groups
               Group group = groupsDao.queryForId(id+"");
                return RestUtils.successResult(group);
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return RestUtils.errorResult("AccessDenied", "You must be logged to get a group's data");
    }

    @POST
    @Produces("application/json")
    public String create(@Context HttpServletRequest request, @FormParam("name") final String name) {
        String res;
        try {
            // Verify if the user is logged and is an administrator.
            if(RestUtils.isLogged(request)) {
                // Verify that the name if not empty.
                if(name != null && !name.isEmpty()) {
                    // Get connection to the database.
                    ConnectionSource conn = DbUtils.getConnection();
                    Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 

                    // Verify that the name is not already being used.
                    final Integer userId = RestUtils.getUserId(request);
                    List<Group> groups = groupsDao.queryForFieldValues(new HashMap<String, Object>() {{
                        put("userId", userId);
                        put("name", name);
                    }});
                    if(groups.isEmpty()) {
                        // Create the group.
                        Group group = new Group();
                        group.setName(name);
                        group.setUserId(userId);
                        groupsDao.create(group);
    
                        // Set sucess response.
                        res = RestUtils.successResult();
                    } else {
                        // Set error message.
                        res = RestUtils.errorResult("InvalidParameters", "The name is already being used in other group");
                    }
                    
                    // Close connection to the database.
                    conn.close();
                } else {
                    // Set error message.
                    res = RestUtils.errorResult("InvalidParameters", "The name can't be empty");
                }
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged to create a group");
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
    public String update(@Context HttpServletRequest request, @PathParam("id") Integer id, @FormParam("name") final String name) {
        String res;
        try {
            // Verify if the user is logged and is an administrator.
            if(RestUtils.isLogged(request)) {
                // Verify that the name if not empty.
                if(name != null && !name.isEmpty()) {
                    // Get connection to the database.
                    ConnectionSource conn = DbUtils.getConnection();
                    Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                    Group group = groupsDao.queryForId(id+"");
                    final Integer userId = RestUtils.getUserId(request);
                    
                    // Verify that the group exists and belongs to the user.
                    if(group != null && Objects.equals(group.getUserId(), userId)) {
                        // Verify that the name is not already being used.
                        List<Group> groups = groupsDao.queryForFieldValues(new HashMap<String, Object>() {{
                            put("userId", userId);
                            put("name", name);
                        }});
                        if(groups.isEmpty() || groups.size() == 1 && Objects.equals(groups.get(0).getId(), id)) {
                            // Update the group.
                            group.setName(name);
                            groupsDao.update(group);

                            // Return response.
                            res = RestUtils.successResult();
                        } else {
                            // Return error message.
                            res = RestUtils.errorResult("InvalidParameters", "The name is already being used by other group");
                        }
                    } else {
                        // Set error message.
                        res = RestUtils.errorResult("InvalidParameters", "The group that you want to update do not exists or do not belongs to your user");
                    }
                    
                    // Close connection to the database.
                    conn.close();
                } else {
                    // Set error message.
                    res = RestUtils.errorResult("InvalidParameters", "The name can't be empty");
                }
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged to update a group"); 
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
            // Verify if the user is logged and is an administrator.
            if(RestUtils.isLogged(request)) {
                    // Get connection to the database.
                    ConnectionSource conn = DbUtils.getConnection();
                    Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                    Dao<Friend,String> friendsDao = DaoManager.createDao(conn, Friend.class); 
                    Group group = groupsDao.queryForId(id+"");
                    final Integer userId = RestUtils.getUserId(request);
                    
                    // Verify that the group exists and belongs to the user.
                    if(group != null && Objects.equals(group.getUserId(), userId)) {
                        // Verify that the group is not used in any contact
                        List<Friend> friends = friendsDao.queryForEq("group_id", id);
                        if(friends.isEmpty()) {
                            // Delete the group.
                            groupsDao.deleteById(id+"");

                            // Return response.
                            res = RestUtils.successResult();
                        } else {
                            // Return error message.
                            res = RestUtils.errorResult("ReferentialConstraint", "You can't delete a group that is being used in some friends");
                        }
                    } else {
                        // Set error message.
                        res = RestUtils.errorResult("InvalidParameters", "The group that you want to delete do not exists or do not belongs to your user");
                    }
                    
                    // Close connection to the database.
                    conn.close();
                } else {
                    // Set error message.
                    res = RestUtils.errorResult("AccessDenied", "You must be logged to delete a group");
                }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return res;
    }
}
