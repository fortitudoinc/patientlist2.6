/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.page.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PersonCountries;
import org.openmrs.module.patientlist.api.PersonCountriesService;
import org.openmrs.ui.framework.page.PageModel;

/**
 * @author levine
 */
public class UpdateNigeriaPatientsCountryPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		System.out.println("*******UpdateNigeriaPatientsCountryPageController");
		List<Patient> patients = Context.getPatientService().getAllPatients();
		List<PersonCountries> pp;
		PersonCountries personCountries;
		int personId;
		for (Patient patient : patients) {
			if (patient.getPersonVoided() || patient.getVoided()) {
				continue;
			}
			personId = patient.getPersonId();
			pp = Context.getService(PersonCountriesService.class).getPersonCountriesForPerson(personId);
			if ((pp == null) || pp.isEmpty()) {
				personCountries = new PersonCountries();
				personCountries.setCountries("NIGERIA");
				personCountries.setDateCreated(new Date());
				personCountries.setPersonId(personId);
				Context.getService(PersonCountriesService.class).savePersonCountries(personCountries);
			}
		}
		
	}
}
