package com.andy.p3listener;

import jakarta.servlet.ServletRequestAttributeEvent;
import jakarta.servlet.ServletRequestAttributeListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class MyRequestAttributeListener implements ServletRequestAttributeListener {

    private static final Logger logger = Logger.getLogger(MyRequestAttributeListener.class.getName());

    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        logger.log(Level.INFO, "Request attribute added: " + srae.getName() + " = " + srae.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        logger.log(Level.INFO, "Request attribute removed: " + srae.getName());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        logger.log(Level.INFO, "Request attribute replaced: " + srae.getName() + " = " + srae.getValue());
    }
}
