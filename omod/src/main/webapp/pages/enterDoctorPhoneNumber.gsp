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
        { label: "Add Doctor Phone Number"}
    ];
</script>

<script>
    function selectDoctor(docIdTelno) {
            var p = docIdTelno.split(",");
            document.getElementById('doctorId').value = p[0];
            document.getElementById('currentTelNo').value = p[1];
    }

function isValidSubmit() {
    if ( (document.getElementById('doctorId').value == 0) ||
         (document.getElementById('telNo').value == 0) ) {
         alert("Please select physician and provide telephone number");
             return false;
             } else {
             return true;
             }
}
</script>

<div>
<form method="post">
<table style="width:100%">
  <tr> <td>
<% if (doctors) { %>   
   <select onchange="selectDoctor(this.value)">
   <option value="0">Select Physician</option>
    <% doctors.each { doc->%>
     <option value="${doc.docId},${doc.telno}">${doc.givenName + " " + doc.familyName}</option>
     <% } %>
    </select>
</td>
<td>Current telephone number: <input type="text" id="currentTelNo" value="" size="35"></td>
</table>

<br><br>
Add/Update Physician Phone Number (include country code; e.g. +25408145663444): <input id="telNo" type="text" name="telNo">
<% } %>
<input id="doctorId" type="hidden" value="0" name="docId">

<input type="submit" value="Submit" onclick='return isValidSubmit();'>
</form>
<div>

<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>

