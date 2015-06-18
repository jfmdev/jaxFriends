package io.github.jfmdev.jaxfriends.dal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "group")
public class Group {
	@DatabaseField(generatedId = true) 
	private Integer id; 
	
	@DatabaseField(canBeNull = false) 
	private String name; 
	
	public Group() {
		this.id = null;
		this.name = "";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
