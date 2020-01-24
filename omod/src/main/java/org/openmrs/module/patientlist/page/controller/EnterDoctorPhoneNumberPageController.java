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
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class EnterDoctorPhoneNumberPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		String drRole = Context.getAdministrationService().getGlobalProperty("patientlist.drrole");
		List<User> users = Context.getUserService().getAllUsers();
		ArrayList<DocAttributes> doctors = new ArrayList<DocAttributes>();
		DocAttributes attributes;
		PersonAttributeType attType = Context.getPersonService().getPersonAttributeTypeByName("Telephone Number");
		for (User user : users) {
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
		}
		model.addAttribute("doctors", doctors);
		System.out.println("*******EnterDoctorPhoneNumberPageController");
		
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "docId", required = false) int docId,
	        @RequestParam(value = "telNo", required = false) String telNo) {
		System.out.println("POST ------------ docId: " + docId + " telno: " + telNo);
		User user = Context.getUserService().getUser(docId);
		Person person = user.getPerson();
		PersonAttributeType attType = Context.getPersonService().getPersonAttributeTypeByName("Telephone Number");
		PersonAttribute personAttribute = new PersonAttribute();
		personAttribute.setPerson(person);
		personAttribute.setDateCreated(new Date());
		personAttribute.setAttributeType(attType);
		personAttribute.setValue(telNo);
		person.addAttribute(personAttribute);
		Context.getPersonService().savePerson(person);
		System.out.println("SAVED USER TELNO: " + user.getPerson().getAttribute(attType).getValue());
		return "redirect:" + "patientlist/enterDoctorPhoneNumber.page";
	}
	
}

class DocAttributes {
	
	String givenName, familyName, telno;
	
	int docId;
	
	DocAttributes(String givenName, String familyName, PersonAttribute telAtt, int docId) {
		this.givenName = givenName;
		this.familyName = familyName;
		if (telAtt == null) {
			telno = "";
		} else {
			telno = telAtt.getValue();
		}
		this.docId = docId;
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
	
	public int getDocId() {
		return docId;
	}
	
	public void setDocId(int docId) {
		this.docId = docId;
	}
	
}
