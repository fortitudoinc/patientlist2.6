package org.openmrs.module.patientlist.page.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.Country;
import org.openmrs.module.patientlist.PersonCountries;
import org.openmrs.module.patientlist.PersonCountry;
import org.openmrs.module.patientlist.api.CountryService;
import org.openmrs.module.patientlist.api.PersonCountriesService;
import org.openmrs.module.patientlist.api.PersonCountryService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class AddCountryInfoPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		System.out.println("*******AddCountryInfoPageController");
		saveCountryInfo(session, "NIGERIA", "+234"); // initialize country list
		saveCountryInfo(session, "GHANA", "+233");
		saveCountryInfo(session, "UGANDA", "+256");
		saveCountryInfo(session, "SIERRA LEONE", "+232");
		saveCountryInfo(session, "LIBERIA", "+231");
		saveCountryInfo(session, "TANZANIA", "+255");
		saveCountryInfo(session, "BURUNDI", "+257");
		saveCountryInfo(session, "LESOTHO", "+266");
		saveCountryInfo(session, "ZIMBABWE", "+263");
		saveCountryInfo(session, "US", "+1");
		List<Country> countries;
		countries = Context.getService(CountryService.class).getAllCountry();
		//movePersonCountriesToPersonCountry(countries);
		model.addAttribute("countries", countries);
		
	}
	
	private void movePersonCountriesToPersonCountry(List<Country> countries) {
		System.out.println("\n\n\nmovePersonCountriesToPersonCountry\n\n\n");
		List<Country> countryList = Context.getService(CountryService.class).getAllCountry();
		HashMap<String, Integer> countryNameToCode = new HashMap<String, Integer>();
		for (Country country : countryList) {
			countryNameToCode.put(country.getName(), country.getId());
		}
		List<PersonCountries> pcs = Context.getService(PersonCountriesService.class).getAllPersonCountries();
		for (PersonCountries personCountries : pcs) {
			int personId = personCountries.getPersonId();
			List<PersonCountry> personCountryList = Context.getService(PersonCountryService.class)
			        .getAllPersonCountryForPerson(personId);
			String[] pCountries = personCountries.getCountries().split(",");
			for (String country : pCountries) {
				if (isPersonCountryDuplicate(country, personCountryList, countryNameToCode)) {
					continue;
				}
				PersonCountry pc = new PersonCountry();
				pc.setCountryId(countryNameToCode.get(country));
				pc.setPersonId(personId);
				pc.setIsVoid(0);
				Context.getService(PersonCountryService.class).savePersonCountry(pc);
			}
		}
	}
	
	private boolean isPersonCountryDuplicate(String country, List<PersonCountry> personCountryList,
	        HashMap<String, Integer> countryNameToCode) {
		System.out.println("\n\n\nisPersonCountryDuplicate, country: " + country + " " + countryNameToCode.size());
		int countryId = countryNameToCode.get(country);
		for (PersonCountry personCountry : personCountryList) {
			if (countryId == personCountry.getCountryId()) {
				return true;
			}
		}
		return false;
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "countryName", required = false) String countryName,
	        @RequestParam(value = "countryCode", required = false) String countryCode) {
		System.out.println("POST ------------ AddCountryInfoPageController: " + countryName + " country code: "
		        + countryCode);
		saveCountryInfo(session, countryName, countryCode);
		return "redirect:" + "patientlist/addCountryInfo.page";
		
	}
	
	private void saveCountryInfo(HttpSession session, String countryName, String countryCode) throws APIException {
		countryName = countryName.toUpperCase();
		if ((countryName.equals("")) || (isCountryDuplicate(countryName))) {
			return;
		}
		Country country = new Country();
		country.setCountryCode(countryCode);
		country.setName(countryName);
		country.setDateCreated(new Date());
		Context.getService(CountryService.class).saveCountry(country);
		InfoErrorMessageUtil.flashInfoMessage(session, countryName + " Saved");
	}
	
	boolean isCountryDuplicate(String countryName) {
		countryName = countryName.trim();
		List<Country> countries = Context.getService(CountryService.class).getAllCountry();
		if (countries.size() == 0) {
			return false;
		}
		for (Country country : countries) {
			if (country.getName().equals(countryName)) {
				return true;
			}
		}
		return false;
	}
}
