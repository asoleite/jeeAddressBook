package com.andre;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.andre.entities.Contact;

public class TestContact {
	
	private String baseURL = "http://localhost:8080/address-book-ws/contact";
	private Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
	
	@Test
	public void testGetContacts() {
		WebTarget webTarget = client.target(baseURL);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		
		Assert.assertEquals(response.getStatus(), 200);
	}

	@Test
	public void testGetByIdContactFail() {
		WebTarget webTarget = client.target(baseURL).path("100");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		Assert.assertEquals(response.getStatus(), 404);
	}

	@Test
	public void testPostContact() {
		WebTarget webTarget = client.target(baseURL);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		Contact c = new Contact();
		c.setAddress("Rua do Fijo");
		c.setName("Andre");
		Set<String> phones = new HashSet<String>();
		phones.add("918712448");
		phones.add("256800100");
		c.setPhoneNumbers(phones);

		Response response = invocationBuilder.post(Entity.entity(c, MediaType.APPLICATION_JSON));

		Assert.assertEquals(response.getStatus(), 201);
	}

	@Test(dependsOnMethods = { "testPostContact" })
	public void testGetByIdContact() {
		WebTarget webTarget = client.target(baseURL).path("1");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		Assert.assertEquals(response.getStatus(), 200);

		Contact c = response.readEntity(Contact.class);

		Assert.assertEquals(c.getId(), 1);

	}
	
	@Test(dependsOnMethods = { "testPostContact" })
	public void testUpdateContact() {
		WebTarget webTarget = client.target(baseURL);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		Contact c = new Contact();
		c.setId(1);
		c.setAddress("Rua do Fijo");
		c.setName("Andre");
		
		
		Set<String> phones = new HashSet<String>();
		phones.add("918712448");
		phones.add("256800100");
		c.setPhoneNumbers(phones);

		Response response = invocationBuilder.put(Entity.entity(c, MediaType.APPLICATION_JSON));

		Assert.assertEquals(response.getStatus(), 200);

	}
	
	@Test(dependsOnMethods = { "testUpdateContact" })
	public void testDeleteContact() {
		WebTarget webTarget = client.target(baseURL).path("1");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.delete();

		Assert.assertEquals(response.getStatus(), 200);

	}
	
	
	@Test(enabled=false)
	public void testDeleteInexistentContact() {
		WebTarget webTarget = client.target(baseURL).path("100");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.delete();

		Assert.assertEquals(response.getStatus(), 404);

	}
	

}
