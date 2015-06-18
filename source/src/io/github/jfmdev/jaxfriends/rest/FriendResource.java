package io.github.jfmdev.jaxfriends.rest;

import io.github.jfmdev.jaxfriends.dal.DBUtils;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

@Path("/friends")
public class FriendResource {
	@GET
	@Produces("application/json")
	public String list() throws Exception {
		return "[{test: 'hola'}, {test: '"+DBUtils.test()+"'}]";
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
