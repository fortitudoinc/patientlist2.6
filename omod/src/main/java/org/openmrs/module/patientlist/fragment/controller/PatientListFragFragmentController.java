/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.fragment.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.Country;
import org.openmrs.module.patientlist.DoctorRequestedByPatient;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.PersonCountry;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.CountryService;
import org.openmrs.module.patientlist.api.DoctorRequestedByPatientService;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.patientlist.api.PatientSpecialtyNeededItemService;
import org.openmrs.module.patientlist.api.PersonCountryService;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import java.util.Random;
import org.apache.commons.codec.binary.Hex;

/**
 * @author levine
 */
public class PatientListFragFragmentController {

    public void controller(HttpServletRequest request, FragmentModel model, HttpSession session) {
        System.out.println("*******PatientListFragFragmentController");

        List<SpecialtyTypeItem> specialtyItems;
        specialtyItems = Context.getService(SpecialtyTypeItemService.class).getAllSpecialtyTypeItem();
        String url = getURL(request);

        String userRole = "";
        ArrayList<String> globalPropertyRoles = new ArrayList<String>();
        String clerkRole, drRole, adminRole;
        clerkRole = Context.getAdministrationService().getGlobalProperty("patientlist.clerkrole");
        drRole = Context.getAdministrationService().getGlobalProperty("patientlist.drrole");
        adminRole = Context.getAdministrationService().getGlobalProperty("patientlist.adminrole");
        globalPropertyRoles.add(clerkRole);
        globalPropertyRoles.add(drRole);
        globalPropertyRoles.add(adminRole);

        User user = Context.getAuthenticatedUser();
        Person person = user.getPerson();
        List<Country> countries = Context.getService(CountryService.class).getAllCountry();
        HashMap<Integer, String> countryMap = getCountryMap(countries);
        List<PersonCountry> pp = Context.getService(PersonCountryService.class).getAllPersonCountryForPerson(
                person.getPersonId());
        String userCountries = getUserCountries(pp, countryMap);
        boolean isUserInNigeria = userCountries.contains("NIGERIA");
        //System.out.println("\n\nuserCountries: " + userCountries);
        /*
		PersonAttributeType attType = Context.getPersonService().getPersonAttributeTypeByName("Country");
		PersonAttribute p = new PersonAttribute();
		p.setPerson(person1);
		p.setDateCreated(new Date());
		p.setAttributeType(attType);
		p.setValue("tanz");
		person.addAttribute(p);
		Context.getPersonService().savePerson(person);
		person = Context.getPersonService().getPersonByUuid(person1.getUuid());
		for (PersonAttribute patt : person.getAttributes(attType.getPersonAttributeTypeId())) {
		System.out.println("PersonId: " + person.getPersonId() + " Country: " + patt.getValue());
		}
         */
        Set<Role> userRoles = user.getAllRoles();
        for (Role role : userRoles) {
            String roleName = role.getName();
            System.out.println("User Role: " + roleName);
            if (globalPropertyRoles.contains(roleName)) {
                userRole = roleName;
                break;
            }
        }
        System.out.println("USER ROLE: " + userRole);

        String patientCountry;
        List<PatientListItem> oldItems = new ArrayList<PatientListItem>();
        ArrayList<PatientListItemLocal> oldPatientListItems = new ArrayList<PatientListItemLocal>();

        if ((userRole.equals(clerkRole)) || (userRole.equals(adminRole))) {
            oldItems = Context.getService(PatientListItemService.class).getOldPatientListItems();
            int oldPatientId;
            for (PatientListItem item : oldItems) {
                oldPatientId = item.getPatientId();
                Patient oldPatient = Context.getPatientService().getPatient(oldPatientId);
                System.out.println("PatientListFragFragmentController oldPatientId: " + oldPatientId);
                if (oldPatient == null) {
                    System.out.println("DIDN'T FIND OLD PATIENT");
                } else {
                    System.out.println("name: " + oldPatient.getGivenName() + " " + oldPatient.getFamilyName());
                }
                if (oldPatient.getVoided()) {
                    continue;
                }
                patientCountry = getPersonCountry(oldPatientId, countryMap);
                if (isPatientCountryInDrCountries(patientCountry, userCountries)) {
                    oldPatientListItems.add(new PatientListItemLocal(item, 0, null, patientCountry, ""));
                }
            }
            Collections.sort(oldPatientListItems, new Comparator<PatientListItemLocal>() {

                public int compare(PatientListItemLocal o1, PatientListItemLocal o2) {
                    return o2.getLastContactAttemptDate().compareTo(o1.getLastContactAttemptDate());
                }
            });
            if (userRole.equals(clerkRole)) {
                userRole = "clerk";
            } else {
                userRole = "admin";
            }
        } else {
            userRole = "dr";
            System.out.println("\n\nUSER ROLE IS DR");
        }

        /*
		Note, when a clerk wishes to add an old patient to the active list she/he must
		be sure the patient has not already been added to the active list; therefore,
		we need to get the active list so the view can do the check
         */
        List<PatientListItem> activeItems = new ArrayList<PatientListItem>();
        ArrayList<PatientListItemLocal> activePatientListItems = new ArrayList<PatientListItemLocal>();
        List<User> doctors = null;
        int patientId, specId;
        User doctorRequested;
        DoctorRequestedAndDoctors docRequestedAndDoctors;
        activeItems = Context.getService(PatientListItemService.class).getActivePatientListItems();
        for (PatientListItem item : activeItems) {
            //activePatientListItems.add(new PatientListItemLocal(item, 0));
            patientId = item.getPatientId();
            Patient activePatient = Context.getPatientService().getPatient(patientId);
            //System.out.println("PatientListFragFragmentController oldPatientId: " + oldPatientId);
            if (activePatient == null) {
                System.out.println("DIDN'T FIND OLD PATIENT");
            } else {
                //System.out.println("name: " + activePatient.getGivenName() + " " + activePatient.getFamilyName());
            }
            if (activePatient.getVoided()) {
                continue;
            }
            String callOption;
            PersonAttribute personCallAtt = activePatient.getAttribute("audioOrVideoCall");
            if (personCallAtt == null) {
                callOption = "audio";
            } else {
                callOption = activePatient.getAttribute("audioOrVideoCall").getValue();
                if ((callOption == null) || (callOption.equals(""))) {
                    callOption = "audio";
                }
            }

            specId = getMostRecentSpecialtyForPatient(patientId);
            //System.out.println("CONTROLLER...patiendId: " + patientId + " dr role: " + drRole + " item.getPatientCallDate: "
            //        + item.getPatientCallDate());
            docRequestedAndDoctors = getRequestedDoctorAndAllDoctors(patientId, drRole, item.getPatientCallDate());
            doctors = docRequestedAndDoctors.getDoctors();
            doctorRequested = docRequestedAndDoctors.getDoctorRequested();
            if (doctorRequested == null) {
                System.out.println("******NO DOCTOR REQUESTED!!");
            } else {
                System.out.println("*****************PatientListFragFragmentController, doctorRequested");
            }
            patientCountry = getPersonCountry(patientId, countryMap);
            if (isPatientCountryInDrCountries(patientCountry, userCountries)) {
                activePatientListItems.add(new PatientListItemLocal(item, specId, doctorRequested, patientCountry,
                        callOption));
            }
        }

        Collections.sort(activePatientListItems, new Comparator<PatientListItemLocal>() {

            public int compare(PatientListItemLocal o1, PatientListItemLocal o2) {
                return o1.patientCallDate.compareTo(o2.patientCallDate);
            }
        });

        model.addAttribute("activePatientListItems", activePatientListItems);
        model.addAttribute("oldPatientListItems", oldPatientListItems);
        model.addAttribute("url", url);
        model.addAttribute("role", userRole);
        model.addAttribute("items", specialtyItems);
        model.addAttribute("doctors", doctors);
        model.addAttribute("numActive", activePatientListItems.size());
        model.addAttribute("isUserInNigeria", isUserInNigeria);
    }

    private String getURL(HttpServletRequest request) {
        String url = (request.getRequestURI()).trim();
        int i = url.indexOf("coreapps");
        if (i < 0) {
            i = url.indexOf("patientlist");
        }
        url = url.substring(0, i);
        i = url.lastIndexOf("/");
        url = url.substring(0, i);
        url = url + "/coreapps/clinicianfacing/patient.page?patientId=";
        return url;
    }

    DoctorRequestedAndDoctors getRequestedDoctorAndAllDoctors(int patientId, String doctorRole,
            Date datePatientListItemCreated) {
        List<User> users = Context.getUserService().getAllUsers();
        ArrayList<User> doctors = new ArrayList<User>();
        User doctorRequested = null;
        int doctorRequestedId = getMostRecentDoctorRequestedByPatientForPatient(patientId, datePatientListItemCreated);
        for (User user : users) {
            Set<Role> userRoles = user.getAllRoles();
            for (Role role : userRoles) {
                String roleName = role.getName();
                if (roleName.equals(doctorRole)) {
                    if (user.getRetired()) {
                        break;
                    }
                    doctors.add(user);
                    if (user.getId() == doctorRequestedId) {
                        doctorRequested = user;
                    }
                    break;
                }
            }
        }
        DoctorRequestedAndDoctors doctorRequestedAndDoctors = new DoctorRequestedAndDoctors(doctorRequested, doctors);
        return doctorRequestedAndDoctors;
    }

    int getMostRecentSpecialtyForPatient(int patientId) {
        List<PatientSpecialtyNeededItem> patientSpecialties;
        patientSpecialties = Context.getService(PatientSpecialtyNeededItemService.class)
                .getPatientSpecialtyNeededItemForPatient(patientId);
        //System.out
        //       .println("getMostRecentSpecialtyForPatient, patient id: " + patientId + " numspecs: " + patientSpecialties);
        if ((patientSpecialties == null) || (patientSpecialties.size() == 0)) {
            return 0;
        }
        PatientSpecialtyNeededItem newItem = patientSpecialties.get(0);
        for (PatientSpecialtyNeededItem item : patientSpecialties) {
            //System.out.println("newitem, item: " + newItem.getDateCreated() + " " + item.getDateCreated());
            if (item.getDateCreated().after(newItem.getDateCreated())) {
                newItem = item;
            }
        }
        //System.out.println("RETURNING: " + newItem.getDateCreated() + " spec: " + newItem.getSpecialtyTypeId());
        return newItem.getSpecialtyTypeId();
    }

    int getMostRecentDoctorRequestedByPatientForPatient(int patientId, Date datePatientListItemCreated) {
        List<DoctorRequestedByPatient> doctorsRequested;
        doctorsRequested = Context.getService(DoctorRequestedByPatientService.class).getDoctorRequestedByPatientForPatient(
                patientId);
        if ((doctorsRequested == null) || (doctorsRequested.size() == 0)) {
            return 0;
        }
        //System.out.println(" getMostRecentDoctorRequestedByPatientForPatient: patientId: " + patientId + " date: "
        //        + datePatientListItemCreated);
        DoctorRequestedByPatient nextRequest = doctorsRequested.get(0);
        for (DoctorRequestedByPatient doctorRequested : doctorsRequested) {
            if (doctorRequested.getDateCreated().after(nextRequest.getDateCreated())) {
                nextRequest = doctorRequested;
            }
        }
        //System.out.println("getMostRecentDoctorRequestedByPatientForPatient, most recent doc id: "
        //      + nextRequest.getDoctorId() + "\n" + "nextreqdate,itemcreateddate: " + nextRequest.getDateCreated() + " "
        //     + datePatientListItemCreated);
        if (nextRequest.getDateCreated().before(datePatientListItemCreated)) {
            //System.out.println("nextRequest.getDoctorId() is before datePatientListItemCreated...return 0");
            return 0;
        }
        //System.out.println("RETURNING ID: " + nextRequest.getDoctorId());
        return nextRequest.getDoctorId();
    }

    private HashMap<Integer, String> getCountryMap(List<Country> countries) {
        HashMap<Integer, String> countryMap = new HashMap<Integer, String>();
        for (Country country : countries) {
            countryMap.put(country.getId(), country.getName());
        }
        return countryMap;
    }

    private String getUserCountries(List<PersonCountry> pp, HashMap<Integer, String> countryMap) {
        String userCountries = "";
        for (PersonCountry personCountry : pp) {
            userCountries += "," + countryMap.get(personCountry.getCountryId());
        }
        return userCountries;
    }

    boolean isPatientCountryInDrCountries(String patientCountry, String drCountries) {
        System.out.println("\n\n\nisPatientCountryInDrCountries, patient: " + patientCountry + "  dr: " + drCountries
                + "\n\n\n");
        return drCountries.contains(patientCountry);
    }

    String getPersonCountry(int personId, HashMap<Integer, String> countryMap) {
        List<PersonCountry> pp = Context.getService(PersonCountryService.class).getAllPersonCountryForPerson(personId);
        if ((pp == null) || pp.isEmpty()) {
            return "";
        }
        System.out.println("\n\ngetPersonCountry, personId: " + personId + "  pp.get(0).getCountryId(): "
                + pp.get(0).getCountryId() + "\n\n\n");
        return countryMap.get(pp.get(0).getCountryId());
    }

    public SimpleObject updateSpecialty(@RequestParam(value = "specId", required = false) String specId,
            @RequestParam(value = "patientId", required = false) String patientId, UiUtils ui) {
        PatientSpecialtyNeededItem ps = new PatientSpecialtyNeededItem();
        ps.setDateCreated(new Date());
        ps.setPatientId(Integer.parseInt(patientId));
        ps.setSpecialtyTypeId(Integer.parseInt(specId));
        Context.getService(PatientSpecialtyNeededItemService.class).savePatientSpecialtyNeededItem(ps);
        String[] properties = new String[]{"dateCreated", "patientId"};
        //System.out.println("SPECIALTY SAVED, SPECIALTY: " + specId + " patientId: " + patientId);
        return SimpleObject.fromObject(ps, ui, "dateCreated", "patientId");
    }

    public SimpleObject updateRequestedDoctor(@RequestParam(value = "docId", required = false) String docId,
            @RequestParam(value = "patientId", required = false) String patientId, UiUtils ui) {

        DoctorRequestedByPatient drRequestedByPatient = new DoctorRequestedByPatient();
        drRequestedByPatient.setDateCreated(new Date());
        drRequestedByPatient.setPatientId(Integer.parseInt(patientId));
        drRequestedByPatient.setDoctorId(Integer.parseInt(docId));
        Context.getService(DoctorRequestedByPatientService.class).saveDoctorRequestedByPatient(drRequestedByPatient);
        String[] properties = new String[]{"dateCreated", "patientId"};
        //System.out.println("REQUESTED DOCTOR SAVED, DOCTOR ID: " + docId + " patientId: " + patientId);
        return SimpleObject.fromObject(drRequestedByPatient, ui, "dateCreated", "patientId");
    }

    public SimpleObject notifyPatientBadWhatsApp(@RequestParam(value = "patientListId", required = false) int patientListId,
            @RequestParam(value = "patientPhoneNum", required = false) String patientPhoneNum, UiUtils ui) {
        PatientListItem patientListItem = Context.getService(PatientListItemService.class).getPatientListItem(patientListId);
        patientListItem.setHasBeenCalled(1);
        User user = Context.getAuthenticatedUser();
        patientListItem.setDrPersonId(user.getPerson().getPersonId());
        Context.getService(PatientListItemService.class).savePatientListItem(patientListItem);

        String msg = "Your doctor is unable to reach you on your WhatsApp. Please activate your WhatsApp "
                + "and request another consult on www.fortitudotelemed.com. Thank you";
        try {
            System.out.println("\n\n\nSENDING MSG FOR BAD WHATSAPP\npatientListId: " + patientListId + " patientPhoneNum: " + patientPhoneNum);
            sendAwsSms(patientPhoneNum, msg);
            System.out.println("MESSAGE HAS BEEN SENT");
        } catch (Exception e) {
            System.out.println("There was an error sending the sms message:");
            e.printStackTrace();
        }

        return new SimpleObject();
    }

    private void sendAwsSms(String phoneNumber, String msg) {

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

    }
}

class DoctorRequestedAndDoctors {

    List<User> doctors;

    User doctorRequested;

    DoctorRequestedAndDoctors(User doctorRequested, List<User> doctors) {
        this.doctors = doctors;
        this.doctorRequested = doctorRequested;
    }

    public List<User> getDoctors() {
        return doctors;
    }

    public User getDoctorRequested() {
        return doctorRequested;
    }

}

class PatientListItemLocal {

    int id, patientId, contactAttempts, hasBeenCalled;

    Date patientCallDate, lastContactAttemptDate;

    String country;

    String voidedReason;

    String patientPhone, patientName;

    String callOption = "";

    SpecialtyTypeItem specItem;

    User doctorRequested;

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCallOption() {
        return callOption;
    }

    public void setCallOption(String callOption) {
        this.callOption = callOption;
    }

    public PatientListItemLocal(org.openmrs.module.patientlist.PatientListItem item, int specId, User doctorRequested,
            String country, String callOption) {
        //System.out.println("**********************Patient id: " + item.getPatientId() + " Spec id: " + specId
        //        + " Dr Requested: " + doctorRequested);
        if (specId == 0) {
            specItem = null;
        } else {
            specItem = Context.getService(SpecialtyTypeItemService.class).getSpecialtyTypeItem(specId);
        }
        this.callOption = callOption;
        this.doctorRequested = doctorRequested;
        this.country = country;
        id = item.getId();
        patientId = item.getPatientId();
        contactAttempts = item.getContactAttempts();
        hasBeenCalled = item.getHasBeenCalled();
        patientCallDate = item.getPatientCallDate();
        lastContactAttemptDate = item.getLastContactAttemptDate();
        voidedReason = item.getVoidedReason();
        Person person = Context.getPersonService().getPerson(item.getPatientId());
        //System.out.println(person.getFamilyName());
        //patientPhone = "xxxxxx";

        try {
            patientPhone = person.getAttribute("Telephone Number").getValue();
            if (!callOption.equals("audio")) { // whatsapp does not want the "+" to start a telno
                if (patientPhone.startsWith("+")) {
                    patientPhone = patientPhone.substring(1);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("[PATIENTLIST] Null Pointer trying to fetch patient phone, filling in with empty string");
            patientPhone = "";
        }

        try {
            patientName = person.getGivenName() + " " + person.getFamilyName();
        } catch (NullPointerException e) {
            System.out.println("[PATIENTLIST] Null pointer trying to fetch patient name, filling in with empty string");
            patientName = "";
        }

    }

    public SpecialtyTypeItem getSpecItem() {
        return specItem;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getContactAttempts() {
        return contactAttempts;
    }

    public int getHasBeenCalled() {
        return hasBeenCalled;
    }

    public Date getPatientCallDate() {
        return patientCallDate;
    }

    public Date getLastContactAttemptDate() {
        return lastContactAttemptDate;
    }

    public String getVoidedReason() {
        return voidedReason;
    }

}
