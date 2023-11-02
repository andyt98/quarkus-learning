package com.andy.p3listener;

import jakarta.servlet.ServletContextAttributeEvent;
import jakarta.servlet.ServletContextAttributeListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Logger;
import java.util.logging.Level;

@WebListener
public class MyContextAttributeListener implements ServletContextAttributeListener {

    private static final Logger logger = Logger.getLogger(MyContextAttributeListener.class.getName());

    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
        logger.log(Level.INFO, "Context attribute added: " + scae.getName() + " = " + scae.getValue());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scae) {
        logger.log(Level.INFO, "Context attribute removed: " + scae.getName());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent scae) {
        logger.log(Level.INFO, "Context attribute replaced: " + scae.getName() + " = " + scae.getValue());
    }
}
