package io.github.jfmdev.jaxfriends.dal;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.pmw.tinylog.Logger;

public class DBUtils {
    /**
     * Connection string to the database.
     */
    private static String dbUrl = null;
    
    /**
     * Account's username for connect to the database.
     */
    private static String dbUser = null;
    
    /**
     * Account's password for connect to the database.
     */
    private static String dbPass = null;
    
    /**
     * Sets the parameters needs to connect to the database.
     * 
     * @param url The database's connection string.
     * @param user The username of a database's account.
     * @param pass The password of a database's account.
     */
    public static void setConnectionParams(String url, String user, String pass) {
        dbUrl = url;
        dbUser = user;
        dbPass = pass;
    }
    
    /**
     * Get a connection to the database.
     * This connection must be closed after is not longuer need.
     * 
     * @return A connection to the database
     * @throws SQLException 
     */
    public static ConnectionSource getConnection() throws SQLException {
        return new JdbcConnectionSource(dbUrl, dbUser, dbPass);
    }
    
    /**
     * Initialize the database, creating and populating the tables if they were not already defined.
     * 
     * @throws SQLException
     */
    public static void initialize() throws SQLException {
        // Get database connection.
        ConnectionSource connectionSource = getConnection();
        
        // Initialize users table.
        Dao<User,String> usersDao = DaoManager.createDao(connectionSource, User.class); 
        if(!usersDao.isTableExists()) {     
            // Create table.
            TableUtils.createTable(connectionSource, User.class); 
            
            // Add default user.
            User admin = new User();
            admin.setAdmin(true);
            admin.setUsername("admin");
            admin.setPassword(toSHA1("admin"));
            usersDao.create(admin);
        }

        // TODO: Initialize groups tables.
        
        // TODO: Initialize friends table.
        
        // Close database connection.
        connectionSource.close();        
    }
    
    /**
     * Calculates the SHA-1 hash for a string.
     * 
     * @param text A string.
     * @return A string with a SHA-1 hash.
     */
    public static String toSHA1(String text) {
        return text != null? toSHA1(text.getBytes()) : null;
    }
    
    /**
     * Calculates the SHA-1 hash for an array of bytes.
     * 
     * @param array An arrays of bytes.
     * @return A string with a SHA-1 hash.
     */
    public static String toSHA1(byte[] array) {
        MessageDigest md = null;
        try { md = MessageDigest.getInstance("SHA-1"); } catch(NoSuchAlgorithmException e) { Logger.error(e); } 
        String res = array != null && md != null? toHex(md.digest(array)) : null;
        return res;
    }
    
    /**
     * Converts a byte array into an String with hexadecimal values.
     * 
     * @param bytes A byte array.
     * @return A string.
     */
    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }    
}
