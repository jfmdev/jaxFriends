package io.github.jfmdev.jaxfriends.dal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Class that represents entries in the 'user' table.
 * 
 * @author jfmdev
 */
@DatabaseTable(tableName = "user")
public class User {
    /**
     * The user's id.
     */
    @DatabaseField(generatedId = true) 
    private Integer id; 

    /**
     * The user's unique name.
     */
    @DatabaseField(canBeNull = false) 
    private String username; 

    /**
     * The SHA1 hash of the user's password.
     */
    @DatabaseField 
    private String password; 

    /**
     * A boolean indicating if the user has administrator privileges.
     */
    @DatabaseField 
    private Boolean admin; 

    /**
     * Default's constructor.
     */
    public User() {
        this.id = null;
        this.username = "";
        this.password = null;
        this.admin = false;
    }

    /**
     * Get the user's id.
     * 
     * @return The user's id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the user's id.
     * 
     * @param id The user's id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the user's name.
     * 
     * @return The user's name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the user's name.
     * 
     * @param username The user name.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the SHA1 hash of the user's password.
     * 
     * @return The SHA1 hash of the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the SHA1 hash of the user's password.
     * 
     * @param password The SHA1 hash of the user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get a boolean indicating if the user has administrator privileges.
     * 
     * @return 'true' if the user has administrator privileges, 'false' if contrary.
     */
    public Boolean getAdmin() {
        return admin;
    }

    /**
     * Set if the user has administrator privileges.
     * 
     * @param admin 'true' if the user has administrator privileges, 'false' if contrary.
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }    
}
