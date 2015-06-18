package io.github.jfmdev.jaxfriends.dal;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBUtils {
	// TODO: remove method.
	public static long test() throws SQLException {
		// TODO: read DB parameters from property file.
	    ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mysql://localhost:3306/jaxfriends", "root", "");
		Dao<Friend,String> accountDao = DaoManager.createDao(connectionSource, Friend.class); 
		
		// if you need to create the 'accounts' table make this call 
		if(!accountDao.isTableExists()) {
			TableUtils.createTable(connectionSource, Friend.class); 
		}
		
		Friend account = new Friend();
		account.setFirstname("Pepe");
		accountDao.create(account);
		
		long res = accountDao.countOf();
		
		// close the connection source 
		connectionSource.close();
		
		return res;
	}
}
