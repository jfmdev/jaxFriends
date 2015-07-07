package io.github.jfmdev.jaxfriends.dal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "friend")
public class Friend {
    @DatabaseField(generatedId = true) 
    private Integer id; 

    @DatabaseField (foreign = true)
    private Group group; 

    @DatabaseField(canBeNull = false) 
    private String firstname; 

    @DatabaseField 
    private String lastname; 

    @DatabaseField 
    private String telephone; 

    @DatabaseField 
    private String email; 

    public Friend() {
        this.id = null;
        this.firstname = "";
        this.lastname = "";
        this.telephone = null;
        this.email = null;
        this.group = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
