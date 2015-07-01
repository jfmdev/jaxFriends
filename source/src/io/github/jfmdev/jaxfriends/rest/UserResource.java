package io.github.jfmdev.jaxfriends.rest;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.github.jfmdev.jaxfriends.dal.DBUtils;
import static io.github.jfmdev.jaxfriends.dal.DBUtils.getConnection;
import io.github.jfmdev.jaxfriends.dal.Friend;
import io.github.jfmdev.jaxfriends.dal.User;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/users")
public class UserResource {
	@GET
	@Produces("application/json")
	public String list() {
            // TODO: Delete all this.
            long res = -1;
            String message = "";
            
            try{
                ConnectionSource connectionSource = DBUtils.getConnection(); 
                Dao<Friend,String> accountDao = DaoManager.createDao(connectionSource, Friend.class); 

                if(!accountDao.isTableExists()) {
                    TableUtils.createTable(connectionSource, Friend.class); 
                }

                res = accountDao.countOf();
                connectionSource.close();
            }catch(SQLException e) { 
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                message = e.getMessage() + "\n\n" + sw.toString(); 
            }
            
            return "[{test: 'hola'}, {test: '"+res+"'}, {message: '"+message+"'}]";
	}
 
	@GET
	@Path("{id}")
	@Produces("application/json")
	public String findById(@PathParam("id") Integer id) {
		Gson gson = new Gson();
		return gson.toJson(new String[]{"hola", "chau"});
	}
	 
	@POST
	@Produces("application/json")
	public String create(@PathParam("id") Integer id) {
		Gson gson = new Gson();
		return gson.toJson(new String[]{"hola", "chau"});
	}
	 
	@PUT
	@Path("{id}")
	@Produces("application/json")
	public String update(@PathParam("id") Integer id) {
		Gson gson = new Gson();
		return gson.toJson(new String[]{"hola", "chau"});
	}
	 
	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public String delete(@PathParam("id") Integer id) {
		Gson gson = new Gson();
		return gson.toJson(new String[]{"hola", "chau"});
	}
}
