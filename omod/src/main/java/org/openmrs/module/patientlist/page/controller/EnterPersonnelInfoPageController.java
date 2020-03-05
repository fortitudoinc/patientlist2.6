/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.page.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PersonCountries;
import org.openmrs.module.patientlist.api.PersonCountriesService;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class EnterPersonnelInfoPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		//String drRole = Context.getAdministrationService().getGlobalProperty("patientlist.drrole");
		List<User> users = Context.getUserService().getAllUsers();
		ArrayList<PersonnelAttributes> personnel = new ArrayList<PersonnelAttributes>();
		PersonnelAttributes attributes;
		int personId;
		String countries;
		String allCountries[];
		countries = Context.getAdministrationService().getGlobalProperty("patientlist.countries");
		allCountries = countries.split(",");
		PersonAttributeType attType = Context.getPersonService().getPersonAttributeTypeByName("Telephone Number");
		for (User user : users) {
			/*
			Set<Role> userRoles = user.getAllRoles();
			for (Role role : userRoles) {
				String roleName = role.getName();
				if (roleName.equals(drRole)) {
					attributes = new DocAttributes(user.getGivenName(), user.getFamilyName(), user.getPerson().getAttribute(
					    attType), user.getUserId());
					doctors.add(attributes);
					break;
				}
			}
			 */
			if (user.getRetired() || user.getPerson().isPersonVoided() || user.getPerson().isVoided()
			        || user.getPerson().getVoided()) {
				continue;
			}
			personId = user.getPerson().getPersonId();
			List<PersonCountries> pp = Context.getService(PersonCountriesService.class).getPersonCountriesForPerson(
			    user.getPerson().getPersonId());
			if ((pp == null) || (pp.size() == 0)) {
				countries = " ";
			} else {
				countries = pp.get(0).getCountries();
			}
			attributes = new PersonnelAttributes(user.getGivenName(), user.getFamilyName(), user.getPerson().getAttribute(
			    attType), user.getUserId(), countries);
			personnel.add(attributes);
		}
		model.addAttribute("personnel", personnel);
		model.addAttribute("allCountries", allCountries);
		System.out.println("*******EnterDoctorPhoneNumberPageController");
		
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "userId", required = false) int userId,
	        @RequestParam(value = "telNo", required = false) String telNo,
	        @RequestParam(value = "personnelCountries", required = false) String personnelCountries) {
		System.out.println("POST ------------ userId: " + userId + " telno: " + telNo + " personnelCountries: "
		        + personnelCountries);
		
		User user = Context.getUserService().getUser(userId);
		Person person = user.getPerson();
		PersonAttributeType attType = Context.getPersonService().getPersonAttributeTypeByName("Telephone Number");
		if ((telNo != null) && (!telNo.equals(""))) {
			System.out.println("POST ------------telno: " + telNo);
			PersonAttribute personAttribute = new PersonAttribute();
			personAttribute.setPerson(person);
			personAttribute.setDateCreated(new Date());
			personAttribute.setAttributeType(attType);
			personAttribute.setValue(telNo);
			person.addAttribute(personAttribute);
			Context.getPersonService().savePerson(person);
			System.out.println("SAVED USER TELNO: " + user.getPerson().getAttribute(attType).getValue());
		}
		int personId = person.getPersonId();
		PersonCountries p;
		List<PersonCountries> pp = Context.getService(PersonCountriesService.class).getPersonCountriesForPerson(personId);
		if ((pp == null) || (pp.size() == 0)) {
			p = new PersonCountries();
			p.setCountries(personnelCountries);
			p.setDateCreated(new Date());
			p.setPersonId(personId);
		} else {
			p = pp.get(0);
			p.setCountries(personnelCountries);
			p.setDateCreated(new Date());
		}
		
		Context.getService(PersonCountriesService.class).savePersonCountries(p);
		
		return "redirect:" + "patientlist/enterPersonnelInfo.page";
	}
	
}

class PersonnelAttributes {
	
	String givenName, familyName, telno, countries;
	
	int userId;
	
	PersonnelAttributes(String givenName, String familyName, PersonAttribute telAtt, int userId, String countries) {
		this.givenName = givenName;
		this.familyName = familyName;
		if (telAtt == null) {
			telno = "xxxxx";
		} else {
			telno = telAtt.getValue();
		}
		this.userId = userId;
		this.countries = countries;
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public String getTelno() {
		return telno;
	}
	
	public void setTelno(String telno) {
		this.telno = telno;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getCountries() {
		return countries;
	}
	
	public void setCountries(String countries) {
		this.countries = countries;
	}
	
}
