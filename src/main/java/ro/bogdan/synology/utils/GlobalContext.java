package ro.bogdan.synology.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import ro.bogdan.synology.engine.beans.Responsable;
import ro.bogdan.synology.engine.beans.Searchable;

@WebListener
public class GlobalContext implements ServletContextListener{

    public static Map<Searchable, List<Responsable>> history = null;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        history = new HashMap<Searchable, List<Responsable>>();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub
        
    }

}
