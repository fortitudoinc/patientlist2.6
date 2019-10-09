package org.openmrs.module.patientlist.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.PatientListItemShort;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.PatientSpecialtyNeededShort;
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
@Resource(name = RestConstants.VERSION_1 + "/specrequested", supportedClass = PatientSpecialtyNeededShort.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class PatientSpecialtyNeededResource extends DataDelegatingCrudResource<PatientSpecialtyNeededShort> {
	
	@Override
	public NeedsPaging<PatientSpecialtyNeededShort> doGetAll(RequestContext context) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public PatientSpecialtyNeededShort getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	protected void delete(PatientSpecialtyNeededShort t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public void purge(PatientSpecialtyNeededShort t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("patientId");
			description.addProperty("specialty");
			description.addLink("default", ".?v=" + RestConstants.REPRESENTATION_DEFAULT);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("patientId");
			description.addProperty("specialty");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	@Override
	public PatientSpecialtyNeededShort newDelegate() {
		System.out.println("****************newDelegate: ");
		return new PatientSpecialtyNeededShort();
	}
	
	@Override
	public PatientSpecialtyNeededShort save(PatientSpecialtyNeededShort t) {
		System.out.println("****************SAVE: " + t);
		PatientSpecialtyNeededItem patientSpecialtyNeededItem = new PatientSpecialtyNeededItem();
		patientSpecialtyNeededItem.setDateCreated(new Date());
		patientSpecialtyNeededItem.setPatientId(Integer.valueOf(t.getPatientId()));
		int medSpecialtyId = 0;
		int specNeeded = 0;
		List<SpecialtyTypeItem> specs = Context.getService(SpecialtyTypeItemService.class).getAllSpecialtyTypeItem();
		for (SpecialtyTypeItem spec : specs) {
			if (spec.getName().equalsIgnoreCase("Medicine")) {
				medSpecialtyId = spec.getId();
			}
			if (spec.getName().equalsIgnoreCase(t.getSpecialty())) {
				specNeeded = spec.getId();
				patientSpecialtyNeededItem.setSpecialtyTypeId(specNeeded);
			}
		}
		if (specNeeded == 0) {
			patientSpecialtyNeededItem.setSpecialtyTypeId(medSpecialtyId);
		}
		Context.getService(PatientSpecialtyNeededItemService.class).savePatientSpecialtyNeededItem(
		    patientSpecialtyNeededItem);
		return t;
	}
	
	@PropertyGetter("display")
	public String getDisplayString(PatientSpecialtyNeededShort item) {
		return item.getPatientId();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("patientId");
		description.addProperty("specialty");
		return description;
	}
}