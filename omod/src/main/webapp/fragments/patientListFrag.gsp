<script>
jq=jQuery;
jq(function() { 

    document.getElementById('redirectURL').value = OPENMRS_CONTEXT_PATH + "/patientlist/patientList.page";
var x = OPENMRS_CONTEXT_PATH + "/patientlist/patientList.page";
var rl = "${role}";
     jq('#activeTable').dataTable({
            "aaSorting": [],
            "sPaginationType": "full_numbers",
            "bPaginate": true,
            "bAutoWidth": false,
            "bLengthChange": true,
            "bSort": true,
            "bJQueryUI": true
        });

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
</script>

<div>

<div class="Table" id="patientMessageList">
<h2>Active Patient List</h2>
<table id="activeTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
<tr>
    <% if (role.equals("dr")) { %>
<th>Call No Answer</th>
    <% } %>
<th>specialty</th>

<th>Patient Name</th>
<th>Contact Attempts</th> 
<th>Patient Call Date</th>
<th>Last Contact Attempt Date</th>
<th>tel</th>
<th>Dashboard</th>
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
<td>${ ui.format(it.patientCallDate)}</td>
<td>${ ui.format(it.lastContactAttemptDate)}</td>
<td>${ ui.format(it.patientPhone)}</td>

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



