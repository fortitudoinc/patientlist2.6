package org.openmrs.module.patientlist.web.controller;

import java.util.List;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.module.webservices.rest.web.resource.impl.MetadataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;

/**
 * @author levine
 */
@Resource(name = RestConstants.VERSION_1 + "/specialtyItem", supportedClass = SpecialtyTypeItem.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class SpecialtyResource extends DataDelegatingCrudResource<SpecialtyTypeItem> {
	
	public NeedsPaging<SpecialtyTypeItem> doGetAll(RequestContext context) {
		System.out.println("****************doGetAll: " + context);
		return new NeedsPaging<SpecialtyTypeItem>(Context.getService(SpecialtyTypeItemService.class)
		        .getAllSpecialtyTypeItem(), context);
	}
	
	public SpecialtyTypeItem getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	protected void delete(SpecialtyTypeItem t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public void purge(SpecialtyTypeItem t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("name");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("name");
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	public SpecialtyTypeItem newDelegate() {
		System.out.println("****************newDelegate: ");
		return new SpecialtyTypeItem();
	}
	
	public SpecialtyTypeItem save(SpecialtyTypeItem t) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@PropertyGetter("display")
	public String getDisplayString(SpecialtyTypeItem item) {
		return item.getName();
	}
	/*	
		@Override
		public DelegatingResourceDescription getCreatableProperties() {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("name");
			
			return description;
		}
	 */
}
