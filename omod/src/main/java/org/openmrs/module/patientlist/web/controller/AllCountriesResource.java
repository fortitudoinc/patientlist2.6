package org.openmrs.module.patientlist.web.controller;

import java.util.ArrayList;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.AllCountriesREST;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;

/**
 * @author levine
 */
@Resource(name = RestConstants.VERSION_1 + "/allcountries", supportedClass = AllCountriesREST.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class AllCountriesResource extends DataDelegatingCrudResource<AllCountriesREST> {
	
	public NeedsPaging<AllCountriesREST> doGetAll(RequestContext context) {
		System.out.println("****************doGetAll: " + context);
		String countries = Context.getAdministrationService().getGlobalProperty("patientlist.countries");
		AllCountriesREST acr = new AllCountriesREST();
		acr.setCountries(countries);
		ArrayList<AllCountriesREST> acrs = new ArrayList<AllCountriesREST>();
		acrs.add(acr);
		return new NeedsPaging<AllCountriesREST>(acrs, context);
	}
	
	public AllCountriesREST getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	protected void delete(AllCountriesREST t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public void purge(AllCountriesREST t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("countries");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("countries");
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	public AllCountriesREST newDelegate() {
		System.out.println("****************newDelegate: ");
		return new AllCountriesREST();
	}
	
	public AllCountriesREST save(AllCountriesREST t) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@PropertyGetter("display")
	public String getDisplayString(AllCountriesREST item) {
		return item.getCountries();
	}
	
}
