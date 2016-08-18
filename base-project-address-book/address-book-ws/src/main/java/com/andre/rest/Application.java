package com.andre.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("")
public class Application extends ResourceConfig {

    public Application() {
    	register(JacksonFeature.class);
        packages(this.getClass().getPackage().getName());
    }

}