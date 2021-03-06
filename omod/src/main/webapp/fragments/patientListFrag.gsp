<script>
jq=jQuery;
jq(function() { 

    document.getElementById('redirectURL').value = OPENMRS_CONTEXT_PATH + "/patientlist/patientList.page";
var x = OPENMRS_CONTEXT_PATH + "/patientlist/patientList.page";
var rl = "${role}";
if (${numActive} > 0 ) {
     jq('#activeTable').dataTable({
            "aaSorting": [],
            "sPaginationType": "full_numbers",
            "bPaginate": true,
            "bAutoWidth": false,
            "bLengthChange": true,
            "bSort": true,
            "bJQueryUI": true
        });
};

     jq('#oldTable').dataTable({
            "aaSorting": [],
            "sPaginationType": "full_numbers",
            "bPaginate": true,
            "bAutoWidth": false,
            "bLengthChange": true,
            "bSort": true,
            "bJQueryUI": true
        });

        if ("$role" === "dr") {
            document.getElementById("oldPatientMessageList").style.display = "none";
        }

 
});

</script>

<script>

    function addToCallAttempts(id) {
        document.getElementById('itemId').value = id;
        document.getElementById('postType').value = "addToCount";
        document.getElementById('addToList').submit();
    }
    function addToList(id) {
        <% activePatientListItems.each { %>
        if ($it.patientId === id) {
            alert("Patient Already on Patient List");
            return;
        }
        <% } %>

        document.getElementById('patId').value = id;
        document.getElementById('postType').value = "addToList";
        document.getElementById('addToList').submit();
    }

    function selectSpecialty(specId,patientId) {
       document.getElementById('itemId').value = specId;
       document.getElementById('patId').value = patientId;
       jq.getJSON('${ ui.actionLink("updateSpecialty") }',
            {
              'specId':    specId,
              'patientId': patientId,
            })
        .success(function(data) {
        })
        .error(function(xhr, status, err) {
            alert('AJAX error ' + err);
        })    
    };
    
    function selectDoctor(docId,patientId) {
        jq.getJSON('${ ui.actionLink("updateRequestedDoctor") }',
            {
              'docId':    docId,
              'patientId': patientId,
            })
        .success(function(data) {
        })
        .error(function(xhr, status, err) {
            alert('AJAX error ' + err);
        })   
    };


    function notifyPatient(patientListId, patientPhoneNum) {
       jq.getJSON('${ ui.actionLink("notifyPatientBadWhatsApp") }',
            {
              'patientListId':    patientListId,
              'patientPhoneNum': patientPhoneNum,
            })
        .success(function(data) {
            location.reload();
        })
        .error(function(xhr, status, err) {
            alert('AJAX error ' + err);
        })
};

</script>

<style>
button.link { background:none;border:none; }
</style>

<div>

<div class="Table" id="patientMessageList">
<h2>Active Patient List</h2>

<!-- https://faq.whatsapp.com/en/android/26000030/?category=5245251</i><br><br> -->

<table id="activeTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
<tr>
    <% if (role.equals("dr")) { %>
<th>Call No Answer</th>
    <% } %>
<th>Requested Doctor</th> 
<th>Specialty</th>

<th>Patient Name</th>
<th>Contact Attempts</th> 
<th>Country</th>
<th>Mode of Consult</th>
<th>Dashboard</th>
<th>Inactive WhatsApp</th>
<!--

        <% if (role.equals("dr")) { %>
<th>Dashboard</th>
        <% } %>
-->
</tr>
</thead>
<tbody >
  <% if (activePatientListItems) { %>   
     <% activePatientListItems.each { %>
<tr>
<% if (role.equals("dr")) { %>
<td style="cursor:pointer" onclick="addToCallAttempts($it.id)"> <i  class="icon-">&#xf0fe;</i></td>
    <% } %>

<td>
<% if (role.equals("dr")) { %>
   <select onchange="selectDoctor(this.value,${it.patientId})" disabled>
     <% } else { %>
   <select onchange="selectDoctor(this.value,${it.patientId})">
    <% } %>

    <option value="1"> Any</option>
    <% doctors.each { doc->%>
<% if ( (it.doctorRequested != null) && (doc.id == it.doctorRequested.id)) { %>
    <option selected value="${doc.id}">${doc.givenName + " " + doc.familyName}</option>
     <% } else { %>
     <option value="${doc.id}">${doc.givenName + " " + doc.familyName}</option>
     <% } %>
     <% } %>
    </select>
</td>
<td>
   <select onchange="selectSpecialty(this.value,${it.patientId})">
    <% items.each { itt->%>
    <% if ( (it.specItem != null) && (itt.id == it.specItem.id)) { %>
    <option selected value="${itt.id}">${itt.name}</option>
     <% } else { %>

     <option value="${itt.id}">${itt.name}</option>
     <% } %>
     <% } %>

    </select>
</td>

<td>${ ui.format(it.patientName)}</td>
<td>${ ui.format(it.contactAttempts)}</td>
<td>${ ui.format(it.country)}</td>
<td>
<% if ( it.callOption.equals("video")) { %>
            <a href="https://wa.me/${it.patientPhone}">Video Consult</a>
<% } else if (it.callOption.equals("text")) { %>
            <a href="https://wa.me/${it.patientPhone}">Text Consult</a>
<% } else if (it.country.equals("NIGERIA") && isUserInNigeria){ %>
        <a href="tel:${it.patientPhone}">Audio Consult</a>
<% } else { %>
    <a href="https://wa.me/${it.patientPhone}">Audio Consult</a>
<% } %>
</td>
<td>
        <a href=$url$it.patientId>dashboard</a>
</td>
<!--
    <% if (role.equals("dr")) { %>
<td>
        <a href=$url$it.patientId>dashboard</a>
</td>
    <% } %>
-->
<td> 
<button onclick="notifyPatient(${it.id}, ${it.patientPhone})">Notify Patient</button>
</td>
</tr>
    <% } %>
  <% } else { %>
  <tr>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
<!--
<% if (role.equals("dr")) { %>
    <td >&nbsp;</td>
    <% } %>
-->
  </tr>
  <% } %>
</tbody>
</table>

</div>

<div class="Table" id="oldPatientMessageList">
<form id="addToList" method="post">

<h2>Old Patient List</h2>
<table id="oldTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
  <tr>
 <!--        <th>Patient Id</th>   -->
<th>Add To List</th><th>Patient Name</th><th>Patient Call Date</th>
        <th>Last Contact Attempt Date</th><th>tel</th>
  </tr>
</thead>
<tbody >
  <% if (oldPatientListItems) { %>
     <% oldPatientListItems.each { %>
      <tr>
<td style="cursor:pointer" onclick="addToList($it.patientId)"> <i  class="icon-">&#xf0fe;</i></td>
<td>${ ui.format(it.patientName)}</td>
<td>${ ui.format(it.patientCallDate)}</td>
<td>${ ui.format(it.lastContactAttemptDate)}</td>
<td>${ ui.format(it.patientPhone)}</td>
      </tr>
    <% } %>
  <% } else { %>
  <tr>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <% } %>
</tbody>
</table>

<input id="postType" type="hidden" name="postType">
<input id="patId" type="hidden" name="patientIdd" value="0">
<input id="itemId" type="hidden" name="itemId" value="0">
<input id="redirectURL" type="hidden" name="redirectUrl">
</form>
</div>

<div>



