package io.github.jfmdev.jaxfriends.servlet;

import io.github.jfmdev.jaxfriends.dal.DBUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Logger;

/**
 * Servlet invoked when the server is started.
 * 
 * @author jfmdev
 */
public class InitServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        // Gets current context.
        ServletContext context = this.getServletContext();

        // Initialize logging library.
        try{ 
            String filePath = context.getRealPath("/WEB-INF/tinylog.properties");
            Configurator newConfig = Configurator.fromFile(new File(filePath)); 
            newConfig.activate();
        }catch(IOException e) {
            System.err.println(e);
        }
        
        // Initialize database.
        try{ 
            // Read properties file.
            Properties props = new Properties();
            String filePath = context.getRealPath("/WEB-INF/config.properties");
            InputStream fileData = new FileInputStream(filePath);
            props.load(fileData);
            
            // Save connection values.
            DBUtils.setConnectionParams(
                "jdbc:mysql://"+props.getProperty("dbHost")+":"+props.getProperty("dbPort")+"/"+props.getProperty("dbName"), 
                props.getProperty("dbUser"), 
                props.getProperty("dbPass")
            );
            
            // Verify if the tables must be created.
            DBUtils.initialize();
        }catch(IOException|SQLException e) {
            System.err.println(e);
            Logger.error(e);
        }
        
        // Create log entry.
        Logger.info("Initialization servlet executed successfully");
    }
}

