
<% ui.decorateWith("appui", "standardEmrPage") 
ui.includeJavascript("uicommons", "datatables/jquery.dataTables.min.js")
ui.includeCss("uicommons", "datatables/dataTables_jui.css")

%>
<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>

<script type="text/javascript">
   var breadcrumbs = [

       { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "View Patient List"}
    ];
</script>

<script>
jq=jQuery;
jq(function() { 

    document.getElementById('redirectURL').value = OPENMRS_CONTEXT_PATH + "/patientlist/patientList.page";
var x = OPENMRS_CONTEXT_PATH + "/patientlist/patientList.page";
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
</script>

<div>


<div class="Table" id="patientMessageList">
<h2>Active Patient List</h2>
<table id="activeTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
  <tr>
 <!--        <th>Patient Id</th>   -->

    <% if (role.equals("dr")) { %>
        <th>Call No Answer</th>
    <% } %>

<th>Patient Name</th><th>Contact Attempts</th> <th>Patient Call Date</th>
        <th>Last Contact Attempt Date</th><th>tel</th>
        <% if (role.equals("dr")) { %>
               <th>Dashboard</th>
        <% } %>
  </tr>
</thead>
<tbody >
  <% if (activePatientListItems) { %>
     <% activePatientListItems.each { %>
      <tr>
<!-- <td>${ ui.format(it.id)}</td>  -->

    <% if (role.equals("dr")) { %>
        
        <td style="cursor:pointer" onclick="addToCallAttempts($it.id)"> <i  class="icon-">&#xf0fe;</i></td>
    <% } %>

<td>${ ui.format(it.patientName)}</td>
<td>${ ui.format(it.contactAttempts)}</td>
<td>${ ui.format(it.patientCallDate)}</td>
<td>${ ui.format(it.lastContactAttemptDate)}</td>
<td>${ ui.format(it.patientPhone)}</td>

    <% if (role.equals("dr")) { %>
        <td>
        <a href=$url$it.patientId>dashboard</a>
        </td>
    <% } %>
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




<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>