package io.github.jfmdev.jaxfriends.dal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {
	@DatabaseField(generatedId = true) 
	private Integer id; 
	
	@DatabaseField(canBeNull = false) 
	private String username; 
	
	@DatabaseField 
	private String password; 
	
	@DatabaseField 
	private Boolean admin; 
	
	public User() {
		this.id = null;
		this.username = "";
		this.password = null;
		this.admin = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
}
