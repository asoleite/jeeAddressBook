package com.andre.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.andre.adapters.DateFormatterAdapter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity Class Contact.
 * 
 * @author André Leite
 */
@Entity
@XmlRootElement
public class Contact implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	/** {@link #id} - Unique identifier */
	private int id;
	
	/** {@link #name} - Contact Name */
	private String name;
	
	/** {@link #address} - Contact Address */
	private String address;
	
	/** 
	 * {@link #birthDate} - Contact Birth date 
	 * in format yyyy-mm-dd
	 * */
	private Date birthDate;
	
	@Transient
	/** 
	 * {@link #phoneNumbers} - Contact Phone Numbers.
	 *  This field is not persisted in the DB. Is only 
	 *  used as a Collection representation from the
	 *  persisted field {@link #pipedPhoneNumbers} 
	 **/
	private Set<String> phoneNumbers;
	
	@JsonIgnore
	/** 
	 * {@link #pipedPhoneNumbers} - String that represents
	 *  all phone numbers from a contact split by "|".
	 *  This field is persisted in the DB but is
	 *  not marshled by jackson.
	 **/
	private String pipedPhoneNumbers;
	
	public Contact(){
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Set<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(Set<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	@JsonIgnore
	public String getPipedPhoneNumbers() {
		return pipedPhoneNumbers;
	}
	@JsonProperty
	public void setPipedPhoneNumbers(String numbers) {
		this.pipedPhoneNumbers = numbers;
	}
	@XmlJavaTypeAdapter(DateFormatterAdapter.class)
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	@Override
	public String toString(){
		return "Contact@" +hashCode() + " : [ id:"+id +", name:"+name +", address:"+address +", phoneNumbers:"+phoneNumbers+"]";
	}

}
