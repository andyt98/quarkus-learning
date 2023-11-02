package com.andy.p3listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class MyRequestListener implements ServletRequestListener {

    private static final Logger logger = Logger.getLogger(MyRequestListener.class.getName());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        logger.log(Level.INFO,"Request initialized");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        logger.log(Level.INFO,"Request destroyed");
    }
}
