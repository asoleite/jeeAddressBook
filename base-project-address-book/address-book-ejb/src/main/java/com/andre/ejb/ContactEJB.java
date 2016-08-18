package com.andre.ejb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andre.entities.Contact;

/**
 * EJB Class which helds contact bussiness logic .
 * 
 * @author André Leite
 */
@Stateless
@LocalBean
public class ContactEJB {

	private final Logger logger = LoggerFactory.getLogger(ContactEJB.class);
	
	@PersistenceContext EntityManager em;
   
    public ContactEJB() {
    }

    /*
     * Helper method to convert a Set of strings 
     * into a string delimited by pipe char "|".
     */
    private String setToPipedString(Set<String> stringSet){
    	String result = "";
    	StringBuffer sb = new StringBuffer();
    	if(!stringSet.isEmpty()){
    		for(String s: stringSet){
				sb.append(s).append("|");
			}
    		result = sb.toString();
    		result = result.substring(0, result.length()-1);
    	}
    	return result;
    }
    
    /*
     * Helper method to convert a string delimited by 
     * pipe char "|" into a Set of strings.
     */
    private Set<String> pipedStringToSet(String string){
    	Set<String> phones = new HashSet<String>();
    	if(!string.isEmpty()){
    		for(String s : string.split("\\|")){
    			phones.add(s);
    		}
    	}
		return phones;
    }
    
	public void addContact(Contact e) {
		logger.debug("Adding contact : "  +e.toString());
		// Before save set pipedPhoneNumbers from Set of phoneNumbers.
		e.setPipedPhoneNumbers(setToPipedString(e.getPhoneNumbers()));
		em.persist(e);
	}

	public void updateContact(Contact e) {
		logger.debug("Updating contact : "  +e.toString());
		// Before save set pipedPhoneNumbers from Set of phoneNumbers.
		e.setPipedPhoneNumbers(setToPipedString(e.getPhoneNumbers()));
		em.merge(e);
		
	}

	public Contact getContactById(int id) {
		logger.debug("Get contact with id : "  +id);
		Contact c = em.find(Contact.class, id);
		if(c != null){
			// Before return set Set of phoneNumbers from pipedPhoneNumbers.
			c.setPhoneNumbers(pipedStringToSet(c.getPipedPhoneNumbers()));
		}
		return c;
	}

	public void deleteContactById(int id) {
		logger.debug("Removing contact with id : "  +id);
		em.remove(em.find(Contact.class, id));
	}


	@SuppressWarnings("unchecked")
	public List<Contact> getContacts(int offset, int limit) {
		logger.debug("Get contacts: offset : " +offset +" and limit: " +limit);
		Query tmp = em.createQuery("from Contact");
		tmp.setFirstResult(limit*offset);
		tmp.setMaxResults(limit);
	    List<Contact> lc = (List<Contact>) tmp.getResultList();
	    // Before return set Set of phoneNumbers from pipedPhoneNumbers for each contact...
	    for(Contact c : lc){
	    	c.setPhoneNumbers(pipedStringToSet(c.getPipedPhoneNumbers()));
	    }
	    return lc;
	}

	public List<Contact> getContactsByNameLike(String exp) {
		// TODO Auto-generated method stub
		return null;
	}

}
