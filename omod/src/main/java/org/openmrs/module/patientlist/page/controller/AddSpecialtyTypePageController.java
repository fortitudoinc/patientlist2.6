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
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class AddSpecialtyTypePageController {
	
	public void controller(HttpServletRequest request, PageModel model,
	        @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService, HttpSession session) {
		System.out.println("*******AddSpecialtyTypePageController");
		addInitialItems(session);
		List<SpecialtyTypeItem> items;
		items = Context.getService(SpecialtyTypeItemService.class).getAllSpecialtyTypeItem();
		model.addAttribute("items", items);
		
		AppFrameworkService service = Context.getService(AppFrameworkService.class);
		List<AppDescriptor> apps = service.getAllEnabledApps();
		List<org.openmrs.module.appframework.domain.Extension> extensions = service.getAllEnabledExtensions();
		System.out.println("APPS");
		for (AppDescriptor appDesc : apps) {
			System.out.println("APP: " + appDesc.getDescription() + " id: " + appDesc.getId());
		}
		for (org.openmrs.module.appframework.domain.Extension ext : extensions) {
			System.out.println("EXT id: " + ext.getId() + " Label: " + ext.getLabel());
		}
		List<org.openmrs.module.appframework.domain.Extension> firstColumnFragments = appFrameworkService
		        .getExtensionsForCurrentUser("patientDashboard.firstColumnFragments");
		for (org.openmrs.module.appframework.domain.Extension ext : firstColumnFragments) {
			System.out.println("FIRSTCOLUMNEXT id: " + ext.getId() + " Label: " + ext.getLabel());
		}
		List<org.openmrs.module.appframework.domain.Extension> includeFragments = appFrameworkService
		        .getExtensionsForCurrentUser("patientDashboard.includeFragments");
		for (org.openmrs.module.appframework.domain.Extension ext : includeFragments) {
			System.out.println("includeFragmentsEXT id: " + ext.getId() + " Label: " + ext.getLabel());
		}
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "specialtyTypeName", required = false) String specialtyTypeName) {
		System.out.println("POST ------------ specialtyTypeName: " + specialtyTypeName);
		addSpecialty(session, specialtyTypeName);
		return "redirect:" + "patientlist/addSpecialtyType.page";
		
	}
	
	void addInitialItems(HttpSession session) {
		//Medicine Pediatrics Psychiatry Surgery  Dermatology OBGYN
		addSpecialty(session, "Medicine");
		addSpecialty(session, "Pediatrics");
		addSpecialty(session, "Psychiatry");
		addSpecialty(session, "Surgery");
		addSpecialty(session, "Dermatology");
		addSpecialty(session, "OBGYN");
	}
	
	void addSpecialty(HttpSession session, String specialty) {
		if ((specialty.equals("")) || (isSpecialtyDuplicate(specialty))) {
			return;
		}
		SpecialtyTypeItem specItem = new SpecialtyTypeItem();
		specItem.setName(specialty);
		specItem.setDateCreated(new Date());
		Context.getService(SpecialtyTypeItemService.class).saveSpecialtyTypeItem(specItem);
		InfoErrorMessageUtil.flashInfoMessage(session, specialty + " Saved");
		
	}
	
	boolean isSpecialtyDuplicate(String specName) {
		specName = specName.trim();
		List<SpecialtyTypeItem> items = Context.getService(SpecialtyTypeItemService.class).getAllSpecialtyTypeItem();
		if (items.size() == 0) {
			return false;
		}
		for (SpecialtyTypeItem item : items) {
			if (item.getName().equals(specName)) {
				return true;
			}
		}
		return false;
	}
}
