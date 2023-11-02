package com.andy.p3listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class MySessionAttributeListener implements HttpSessionAttributeListener {

    private static final Logger logger = Logger.getLogger(MySessionAttributeListener.class.getName());
    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        logger.log(Level.INFO,"Session attribute added: " + se.getName() + " = " + se.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        logger.log(Level.INFO,"Session attribute removed: " + se.getName());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        logger.log(Level.INFO,"Session attribute replaced: " + se.getName() + " = " + se.getValue());
    }
}
