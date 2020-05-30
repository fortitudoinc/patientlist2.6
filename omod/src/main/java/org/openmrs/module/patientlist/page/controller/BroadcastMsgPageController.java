/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.page.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.sns.AmazonSNSClient;
//import com.amazonaws.services.sns.model.CreateTopicRequest;
//import com.amazonaws.services.sns.model.CreateTopicResult;
import java.util.Random;
import org.apache.commons.codec.binary.Hex;

/**
 * @author levine
 */
public class BroadcastMsgPageController {
	
	String allCountries = "All Countries";
	
	public void controller(HttpServletRequest request, PageModel model) {
		String countryList;
		ArrayList<String> countries = new ArrayList<String>();
		countries.add(allCountries);
		countryList = Context.getAdministrationService().getGlobalProperty("patientlist.countries");
		String[] countryArray = countryList.split(",");
		for (String country : countryArray) {
			countries.add(country);
		}
		model.addAttribute("countries", countries);
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "country", required = false) String country,
	        @RequestParam(value = "message", required = false) String message) {
		System.out.println("POST ------------ BroadcastMsgPageController: " + country + " " + message);
		
		List<String> telNos = getPatientTelNumbersFromCountry(country);
		telNos = filterForDuplicateTelNumbers(telNos);
		for (String telNo : telNos) {
			System.out.println("Sending to: " + telNo + " Message: " + message);
			sendAwsSms(telNo, message);
		}
		
		return "redirect:" + "patientlist/broadcastMsg.page";
		
	}
	
	List<String> getPatientTelNumbersFromCountry(String country) {
		List<Patient> patients = Context.getPatientService().getAllPatients();
		System.out.println("PATIENTS\n");
		for (Patient patient : patients) {
			System.out.println(patient.getGivenName() + " TEL: " + patient.getAttribute("Telephone Number").getValue());
		}
		List<String> telNos = new ArrayList<String>();
		String countryAndCodeList = Context.getAdministrationService().getGlobalProperty("patientlist.countrycodes");
		String[] countriesAndCodes = countryAndCodeList.split(",");
		// each array element has Country/CountryCode
		String countryCode;
		if (country.equals(allCountries)) {
			telNos = getPatientTelNumbersWithCountryCode(patients, "");
		}
		for (String countryAndCode : countriesAndCodes) {
			if (countryAndCode.startsWith(country)) {
				String[] countryAndCodeArray = countryAndCode.split("/");
				countryCode = countryAndCodeArray[1];
				telNos = getPatientTelNumbersWithCountryCode(patients, countryCode);
				System.out.println("Country Code: " + countryCode);
				break;
			}
		}
		return filterForDuplicateTelNumbers(telNos);
	}
	
	List<String> getPatientTelNumbersWithCountryCode(List<Patient> patients, String countryCode) {
		ArrayList<String> telNos = new ArrayList<String>();
		String telNumber;
		for (Patient patient : patients) {
			telNumber = patient.getAttribute("Telephone Number").getValue();
			if (telNumber.startsWith(countryCode)) {
				telNos.add(telNumber);
				System.out.println("Adding: " + telNumber);
			}
		}
		System.out.println("Patients with code: " + countryCode + " number: " + telNos.size());
		return telNos;
	}
	
	List<String> filterForDuplicateTelNumbers(List<String> telNos) {
		ArrayList<String> finalTelNos = new ArrayList<String>();
		if (telNos.size() == 0) {
			return finalTelNos;
		}
		if (telNos.size() == 1) {
			finalTelNos.add(telNos.get(0));
			return finalTelNos;
		}
		telNos.sort(null);
		for (int i = 0; i < telNos.size() - 1; i++) {
			if (telNos.get(i).equals(telNos.get(i + 1))) {
				continue;
			}
			finalTelNos.add(telNos.get(i));
		}
		System.out.println("telNos.size: " + telNos.size());
		finalTelNos.add(telNos.get(telNos.size() - 1));
		return finalTelNos;
	}
	
	private void sendAwsSms(String phoneNumber, String msg) {
		/*
		// Generate a random topic name to avoid collisions
		byte[] b = new byte[20];
		new Random().nextBytes(b);
		String tmpTopicName = "physician-message-tmp-" + Hex.encodeHexString(b);
		// Initialize AWS client
		AmazonSNSClient snsClient = new AmazonSNSClient();
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		
		// Create the topic
		CreateTopicResult createTopicResult = snsClient.createTopic(new CreateTopicRequest(tmpTopicName));
		snsClient.subscribe(createTopicResult.getTopicArn(), "SMS", phoneNumber);
		snsClient.publish(createTopicResult.getTopicArn(), msg);
		
		// Cleanup
		snsClient.deleteTopic(createTopicResult.getTopicArn());
		*/
		
	}
}
