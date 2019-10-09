package org.openmrs.module.patientlist.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.PatientListItemShort;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.patientlist.api.PatientSpecialtyNeededItemService;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * @author levine
 */
@Resource(name = RestConstants.VERSION_1 + "/activepatientlistitems", supportedClass = PatientListItemShort.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class PatientListItemsResource extends DataDelegatingCrudResource<PatientListItemShort> {
	
	@Override
	public NeedsPaging<PatientListItemShort> doGetAll(RequestContext context) {
		System.out.println("****************PatientListItemsResource: doGetAll");
		
		List<PatientListItem> patItems = Context.getService(PatientListItemService.class).getActivePatientListItems();
		List<PatientListItemShort> patItemsShort = new ArrayList<PatientListItemShort>();
		System.out.println("NUM ACTIVE PATIENTS: " + patItems.size());
		for (PatientListItem patItem : patItems) {
			PatientListItemShort patItemShort = new PatientListItemShort();
			System.out.println("Active patient: " + patItem.getPatientId());
			patItemShort.setPatientId(String.valueOf(patItem.getPatientId()));
			patItemShort.setIsOnActivePatientList("true");
			patItemsShort.add(patItemShort);
		}
		return new NeedsPaging<PatientListItemShort>(patItemsShort, context);
	}
	
	@Override
	public PatientListItemShort getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	protected void delete(PatientListItemShort t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public void purge(PatientListItemShort t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("patientId");
			description.addProperty("isOnActivePatientList");
			description.addLink("default", ".?v=" + RestConstants.REPRESENTATION_DEFAULT);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("patientId");
			description.addProperty("isOnActivePatientList");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	@Override
	public PatientListItemShort newDelegate() {
		System.out.println("****************newDelegate: ");
		return new PatientListItemShort();
	}
	
	@Override
	public PatientListItemShort save(PatientListItemShort t) {
		System.out.println("****************SAVE: " + t);
		PatientListItem patientListItem = new PatientListItem();
		patientListItem.setPatientCallDate(new Date());
		patientListItem.setLastContactAttemptDate(new Date());
		patientListItem.setContactAttempts(0);
		patientListItem.setHasBeenCalled(0);
		patientListItem.setVoidedReason("not voidedd");
		patientListItem.setPatientId(Integer.valueOf(t.getPatientId()));
		patientListItem.setClerkPersonId(1);
		patientListItem.setDrPersonId(1);
		
		PatientSpecialtyNeededItem specialtyItemNeeded = new PatientSpecialtyNeededItem();
		specialtyItemNeeded.setDateCreated(new Date());
		specialtyItemNeeded.setPatientId(Integer.valueOf(t.getPatientId()));
		// default to Medicine
		int medicineId;
		List<SpecialtyTypeItem> specialties = Context.getService(SpecialtyTypeItemService.class).getAllSpecialtyTypeItem();
		for (SpecialtyTypeItem specItem : specialties) {
			if (specItem.getName().equalsIgnoreCase("Medicine")) {
				specialtyItemNeeded.setSpecialtyTypeId(specItem.getId());
				break;
			}
		}
		
		Context.getService(PatientSpecialtyNeededItemService.class).savePatientSpecialtyNeededItem(specialtyItemNeeded);
		patientListItem = Context.getService(PatientListItemService.class).savePatientListItem(patientListItem);
		System.out.println("****************ITEM SAVED: ");
		return t;
	}
	
	@PropertyGetter("display")
	public String getDisplayString(PatientListItemShort item) {
		return item.getPatientId();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("patientId");
		
		return description;
	}
}
