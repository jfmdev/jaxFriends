package io.github.jfmdev.jaxfriends.rest;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.ColumnArg;
import com.j256.ormlite.stmt.QueryBuilder;
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

@Path("/friends")
public class FriendResource {
    @GET
    @Produces("application/json")
    public String list(@Context HttpServletRequest request) throws Exception {
        try{
            // Verify if the user is logged.
            if(RestUtils.isLogged(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<Friend,String> friendsDao = DaoManager.createDao(conn, Friend.class); 
                Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                Integer userId = RestUtils.getUserId(request);
                
                // Search all friends from the user.
                QueryBuilder<Group, String> groupQuery = groupsDao.queryBuilder();
                groupQuery.where().eq("userId", userId);
                QueryBuilder<Friend, String> friendQuery = friendsDao.queryBuilder();
                friendQuery.where().eq("group_id", new ColumnArg("group", "id"));
                List<Friend> friends = friendQuery.join(groupQuery).query();
                
                // Close connecto to the database and return result.
                conn.close();
                return RestUtils.successResult(friends);
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return RestUtils.errorResult("AccessDenied", "You must be logged to get the list of friends");
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public String findById(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        String res;
        try{
            // Verify if the user is logged.
            if(RestUtils.isLogged(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<Friend,String> friendsDao = DaoManager.createDao(conn, Friend.class); 
                Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                Integer userId = RestUtils.getUserId(request);
                
                // Search the friend.
                QueryBuilder<Group, String> groupQuery = groupsDao.queryBuilder();
                groupQuery.where().eq("userId", userId);
                QueryBuilder<Friend, String> friendQuery = friendsDao.queryBuilder();
                friendQuery.where().eq("group_id", new ColumnArg("group", "id")).and().eq("id", id);
                Friend friend = friendQuery.join(groupQuery).queryForFirst();
                conn.close();
               
                // Verify if the friend was found.
                if(friend != null) {
                    res = RestUtils.successResult(friend);
                } else {
                    res = RestUtils.errorResult("InvalidParameters", "The friend do not exists or do not belongs to the user");
                }
            } else {
                res = RestUtils.errorResult("AccessDenied", "You must be logged to get a friend's information");
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
            @FormParam("group_id") final Integer groupId,
            @FormParam("first_name") final String firstName,
            @FormParam("last_name") final String lastName,
            @FormParam("telephone") final String telephone,
            @FormParam("email") final String email) {
        String res;
        try {
            // Verify if the user is logged.
            if(RestUtils.isLogged(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<Friend,String> friendsDao = DaoManager.createDao(conn, Friend.class); 
                Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                final Integer userId = RestUtils.getUserId(request);

                // Verify that the group belongs to the user.
                Group group = groupsDao.queryForId(groupId+"");
                if(group != null && Objects.equals(group.getUserId(), userId)) {
                    Friend friend = new Friend();
                    friend.setFirstname(firstName);
                    friend.setLastname(lastName);
                    friend.setTelephone(telephone);
                    friend.setEmail(email);
                    friend.setGroup(group);
                    friendsDao.create(friend);

                    // Set sucess response.
                    res = RestUtils.successResult();                
                } else {
                    // Set error message.
                    res = RestUtils.errorResult("InvalidParameters", "The group do not exists or do not belongs to the user");
                }

                // Close connection to the database.
                conn.close();
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged to create a friend");
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return res;    }

    @PUT
    @Path("{id}")
    @Produces("application/json")
    public String update(@Context HttpServletRequest request, 
            @PathParam("id") Integer id,
            @FormParam("group_id") final Integer groupId,
            @FormParam("first_name") final String firstName,
            @FormParam("last_name") final String lastName,
            @FormParam("telephone") final String telephone,
            @FormParam("email") final String email ){
        String res;
        try {
            // Verify if the user is logged.
            if(RestUtils.isLogged(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                Dao<Friend,String> friendsDao = DaoManager.createDao(conn, Friend.class); 
                final Integer userId = RestUtils.getUserId(request);

                // Verify that the friend exists and belongs to the user.
                QueryBuilder<Group, String> groupQuery = groupsDao.queryBuilder();
                groupQuery.where().eq("userId", userId);
                QueryBuilder<Friend, String> friendQuery = friendsDao.queryBuilder();
                friendQuery.where().eq("group_id", new ColumnArg("group", "id")).and().eq("id", id);
                Friend friend = friendQuery.join(groupQuery).queryForFirst();
                
                if(friend != null) {
                    // Verify that the group belongs to the user.
                    Group group = groupsDao.queryForId(groupId+"");
                    if(group != null && Objects.equals(group.getUserId(), userId)) {
                        friend.setFirstname(firstName);
                        friend.setLastname(lastName);
                        friend.setTelephone(telephone);
                        friend.setEmail(email);
                        friend.setGroup(group);
                        friendsDao.update(friend);

                        // Set sucess response.
                        res = RestUtils.successResult();                
                    } else {
                        // Set error message.
                        res = RestUtils.errorResult("InvalidParameters", "The group do not exists or do not belongs to the user");
                    }
                } else {
                    // Set error message.
                    res = RestUtils.errorResult("InvalidParameters", "The friend do not exists or do not belongs to the user");
                }

                // Close connection to the database.
                conn.close();
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged to modify a friend");
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
            if(RestUtils.isLogged(request)) {
                // Get connection to the database.
                ConnectionSource conn = DbUtils.getConnection();
                Dao<Group,String> groupsDao = DaoManager.createDao(conn, Group.class); 
                Dao<Friend,String> friendsDao = DaoManager.createDao(conn, Friend.class); 
                final Integer userId = RestUtils.getUserId(request);

                // Verify that the friend exists and belongs to the user.
                QueryBuilder<Group, String> groupQuery = groupsDao.queryBuilder();
                groupQuery.where().eq("userId", userId);
                QueryBuilder<Friend, String> friendQuery = friendsDao.queryBuilder();
                friendQuery.where().eq("group_id", new ColumnArg("group", "id")).and().eq("id", id);
                Friend friend = friendQuery.join(groupQuery).queryForFirst();
                
                if(friend != null) {
                    // Delete friend.
                    friendsDao.deleteById(id+"");

                    // Return response.
                    res = RestUtils.successResult();                    
                } else {
                    // Set error message.
                    res = RestUtils.errorResult("InvalidParameters", "The friend do not exists or do not belongs to the user");
                }

                // Close connection to the database.
                conn.close();
            } else {
                // Set error message.
                res = RestUtils.errorResult("AccessDenied", "You must be logged to delete a friend");
            }
        }catch(SQLException e) {
            // Log error and return error message.
            Logger.error(e);
            return RestUtils.errorResult(e);
        }
        return res;
    }
}
