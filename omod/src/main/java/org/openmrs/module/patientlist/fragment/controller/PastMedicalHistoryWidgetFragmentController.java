/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.fragment.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageModel;

/**
 * @author levine
 */
public class PastMedicalHistoryWidgetFragmentController {
	
	public void controller(FragmentModel model, @FragmentParam("patientId") Patient patient, UiUtils ui,
	        HttpServletRequest request, HttpSession session) {
		int patientId = patient.getPatientId();
		List<Visit> visits = Context.getVisitService().getActiveVisitsByPatient(patient);
		String link;
		String returnURL = request.getRequestURL() + "?patientId=" + patientId + "&";
		if (visits.size() == 0) {
			link = returnURL;
		} else {
			String visitUUId = visits.get(0).getUuid();
			String formUuid = Context.getAdministrationService().getGlobalProperty("patientlist.pastmedicalhistoryformUUID");
			returnURL = returnURL.substring(returnURL.indexOf(request.getContextPath()));
			if (returnURL.indexOf("openmrs-standalone") < 0) {
				returnURL = returnURL.substring(1); //remove the leading slash
			}
			returnURL = request.getRequestURL() + "?" + request.getQueryString();
			link = request.getRequestURL().substring(0, request.getRequestURL().indexOf("coreapps"))
			        + "htmlformentryui/htmlform/enterHtmlFormWithStandardUi.page?";
			link += "patientId=" + patientId + "&visitId=" + visitUUId + "&formUuid=" + formUuid + "&returnUrl=" + returnURL;
			System.out.println("\nrequest.getRequestURI(): " + request.getRequestURI() + "\nrequest.getQueryString(): "
			        + request.getQueryString() + "\nrequest.getRequestURL(): " + request.getRequestURL());
			System.out.println("\nrequest.getContextPath(): " + request.getContextPath() + "\nrequest.getPathInfo(): "
			        + request.getPathInfo() + "\nrequest.getPathTranslated(): " + request.getPathTranslated()
			        + "\nrequest.getProtocol(): " + request.getProtocol() + "\nrequest.getLocalPort(): "
			        + request.getLocalPort());
		}
		if (link.contains(":443/")) {
			link = link.replaceAll("http:", "https:");
			link = link.replaceAll(":443", "");
		}
		System.out.println("\n\nLINK: " + link);
		
		Person person = patient.getPerson();
		Concept concept = Context.getConceptService().getConceptByName("pastMedicalHistory");
		List<Obs> allObs = Context.getObsService().getObservationsByPersonAndConcept(person, concept);
		ArrayList<String> allHistory = new ArrayList<String>();
		for (Obs obs : allObs) {
			getLines(allHistory, obs.getValueText());
		}
		model.addAttribute("link", link);
		model.addAttribute("allHistory", allHistory);
	}
	
	void getLines(ArrayList<String> allHistory, String obsText) {
		final int lf = 10, cr = 13;
		String nextLine = "";
		char nextChar;
		for (int i = 0; i < obsText.length(); i++) {
			nextChar = obsText.charAt(i);
			if ((nextChar == lf) || (nextChar == cr)) {
				nextLine = nextLine.trim();
				allHistory.add(nextLine);
				nextLine = "";
				if ((i + 1 < obsText.length())) {
					nextChar = obsText.charAt(i + 1);
					if ((nextChar == lf) || (nextChar == cr)) {
						i++; //skip second of cr/lf pair
					}
				}
			} else {
				nextLine += Character.valueOf(nextChar);
			}
		}
		nextLine = nextLine.trim();
		if (nextLine.length() > 0) {
			allHistory.add(nextLine);
		}
	}
}
