package io.github.jfmdev.jaxFriends.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

@Path("/account")
public class AccountResource {
	@POST
	@Path("/login")
	@Produces("application/json")
	public String login(@PathParam("id") Integer id) {
		Gson gson = new Gson();
		return gson.toJson(new String[]{"hola", "chau"});
	}
	 
	@GET
	@Path("/logout")
	@Produces("application/json")
	public String logout(@PathParam("id") Integer id) {
		Gson gson = new Gson();
		return gson.toJson(new String[]{"hola", "chau"});
	}
	 
	@PUT
	@Produces("application/json")
	public String udpate(@PathParam("id") Integer id) {
		Gson gson = new Gson();
		return gson.toJson(new String[]{"hola", "chau"});
	}
}
