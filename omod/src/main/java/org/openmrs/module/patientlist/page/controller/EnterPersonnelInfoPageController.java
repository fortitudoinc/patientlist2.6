/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.page.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.Country;
import org.openmrs.module.patientlist.PersonCountry;
import org.openmrs.module.patientlist.api.CountryService;
import org.openmrs.module.patientlist.api.PersonCountryService;
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
		List<Country> allCountries = Context.getService(CountryService.class).getAllCountry();
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
			List<PersonCountry> personCountries = Context.getService(PersonCountryService.class)
			        .getAllPersonCountryForPerson(personId);
			attributes = new PersonnelAttributes(user.getGivenName(), user.getFamilyName(), user.getPerson().getAttribute(
			    attType), user.getUserId(), personCountries);
			personnel.add(attributes);
		}
		model.addAttribute("personnel", personnel);
		model.addAttribute("allCountries", allCountries);
		System.out.println("*******EnterDoctorPhoneNumberPageController");
		
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "userId", required = false) int userId,
	        @RequestParam(value = "telNo", required = false) String telNo,
	        @RequestParam(value = "personnelCountryIds", required = false) String personnelCountryIds) {
		System.out.println("POST ------------ userId: " + userId + " telno: " + telNo + " personnelCountryIds: "
		        + personnelCountryIds);
		
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
		List<PersonCountry> pp = Context.getService(PersonCountryService.class).getAllPersonCountryForPerson(personId);
		String newCountryIds = "," + personnelCountryIds + ",";
		if (!((newCountryIds == null) || (newCountryIds.equals("")))) {
			setNewPersonCountryList(personId, newCountryIds, pp);
		}
		return "redirect:" + "patientlist/enterPersonnelInfo.page";
	}
	
	private void setNewPersonCountryList(int personId, String newCountryIds, List<PersonCountry> pp) {
		System.out.println("\n\n\nsetNewPersonCountryList,personId: " + personId + " new ids: " + newCountryIds + " \n\n\n");
		for (PersonCountry oldPersonCountry : pp) {
			int oldCountryId = oldPersonCountry.getCountryId();
			String oldCountryIdString = String.valueOf(oldCountryId);
			if (newCountryIds.contains(oldCountryIdString)) {
				newCountryIds = newCountryIds.replace(oldCountryIdString, "");
				continue;
			}
			// old country is no longer in personnel list of countries so make it void
			oldPersonCountry.setIsVoid(1);
			Context.getService(PersonCountryService.class).savePersonCountry(oldPersonCountry);
		}
		if (!newCountryIds.matches(".*\\d.*")) {
			return;
		}
		String[] newCountryIdsToAdd = newCountryIds.split(",");
		System.out.println("\n\n\nsetNewPersonCountryList, newCountryIdsToAdd" + newCountryIds);
		for (String newCountryIdToAdd : newCountryIdsToAdd) {
			if (newCountryIdToAdd.equals("")) {
				continue;
			}
			PersonCountry p = new PersonCountry();
			p.setCountryId(Integer.valueOf(newCountryIdToAdd));
			p.setDateCreated(new Date());
			p.setPersonId(personId);
			Context.getService(PersonCountryService.class).savePersonCountry(p);
		}
		
	}
	
}

class PersonnelAttributes {
	
	String givenName, familyName, telno;
	
	int userId;
	
	ArrayList<Integer> personCountriesIds = new ArrayList<Integer>();
	
	PersonnelAttributes(String givenName, String familyName, PersonAttribute telAtt, int userId,
	    List<PersonCountry> personCountries) {
		this.givenName = givenName;
		this.familyName = familyName;
		if (telAtt == null) {
			telno = "xxxxx";
		} else {
			telno = telAtt.getValue();
		}
		this.userId = userId;
		for (PersonCountry pc : personCountries) {
			personCountriesIds.add(pc.getCountryId());
		}
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
	
	public ArrayList<Integer> getPersonCountriesIds() {
		return personCountriesIds;
	}
	
	public void setPersonCountriesIds(ArrayList<Integer> personCountriesIds) {
		this.personCountriesIds = personCountriesIds;
	}
	
}
