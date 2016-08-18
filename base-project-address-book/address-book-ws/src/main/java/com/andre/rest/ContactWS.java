package com.andre.rest;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andre.ejb.ContactEJB;
import com.andre.entities.Contact;


@Path("contact")
@Stateless
public class ContactWS {

	private final Logger logger = LoggerFactory.getLogger(ContactWS.class);
	
	@EJB
	private ContactEJB cEJB;

	private void lookupEJBs() throws ServletException {
		if (cEJB == null) {
			try {
				InitialContext ic = new InitialContext();
				cEJB = (ContactEJB) ic.lookup("java:comp/env/ejb/ContactEJB");
			} catch (NamingException e) {
				logger.error("Error in ejb lookup", e);
				throw new ServletException(e);
			} catch (Exception e) {
				logger.error("Error in ejb lookup", e);
				throw new ServletException(e);
			}
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getContacts(@QueryParam("results") Integer results, @QueryParam("page") Integer page) {
		try {
			logger.debug("getContacts: results="+results +" page="+page);
			lookupEJBs();
			int offset = page == null ? 0 : page - 1;
			int limit = results == null ? 20 : results;
			return Response.status(Status.OK).entity(cEJB.getContacts(offset, limit)).build(); 
		} catch (Throwable e) {
			logger.error("Error getContacts", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response postContact(Contact to) {
		try {
			logger.debug("postContact: contact:"+to.toString());
			lookupEJBs();
			cEJB.addContact(to);
			return Response.status(Status.CREATED).build();
		} catch (Throwable e) {
			logger.error("Error postContact", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response getContactById(@PathParam("id") int id) {
		try {
			logger.debug("getContactById: id:"+id);
			lookupEJBs();
			Contact c = cEJB.getContactById(id);
			if(c == null){
				return Response.status(Status.NOT_FOUND).build(); 
			}
			return Response.status(Status.OK).entity(c).build(); 
		} catch (Throwable e) {
			logger.error("Error getContactById", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response deleteContactById(@PathParam("id") int id) {
		try {
			logger.debug("deleteContactById: id:"+id);
			lookupEJBs();
			cEJB.deleteContactById(id);
			return Response.status(Status.OK).build();
		} catch (Throwable e) {
			logger.error("Error deleteContactById", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response updateContact(Contact to) {
		try {
			logger.debug("updateContact: contact:"+to.toString());
			cEJB.updateContact(to);
			return Response.status(Status.OK).build(); 
		} catch (Throwable e) {
			logger.error("Error updateContact", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
