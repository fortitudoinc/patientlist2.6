Module: patientList

Privilege needed to run module: "App: patient list"

Privilege needed to view reports: "App: patient report"

See Global Privilges below (step 3)

The admin must create three roles:

1. A clerk role: the clerk will be responsible for adding patients
	to the patient list either by registering a new patient
	or adding a previously registered patient. The clerk can view
	the previously registered patients on their user interface via
	the app on their home page. Also, the clerk
	will obtain the report on how many patients were seen by
	each doctor; doctor payments are based on number of patients
	seen for each pay period. 
2. A doctor role: the doctor will see the list of active patients
	and attempt to call a patient who is at the top of the list.
3. An admin role: the admin will be able to view both active and
	previously seen patients. In addition, the admin will need to
	create the 3 new roles, e.g. "clerk", "dr", and "admin". The 3 roles
	should have the aforementioned privilege, in addition to other roles
	as needed. The "clerk" role will need the report privilege, as noted, 
	above. The admin should set the patient list global properties
	as indicated therein.
