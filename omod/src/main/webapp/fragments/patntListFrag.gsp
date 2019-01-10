

<script type="text/javascript">
jq=jQuery;
jq(function() { 
     jq('#specialtyTable').dataTable({
            "aaSorting": [],
            "sPaginationType": "full_numbers",
            "bPaginate": true,
            "bAutoWidth": false,
            "bLengthChange": true,
            "bSort": true,
            "bJQueryUI": true
        });
     jq('#activeTable').dataTable({
            "aaSorting": [],
            "sPaginationType": "full_numbers",
            "bPaginate": true,
            "bAutoWidth": false,
            "bLengthChange": true,
            "bSort": true,
            "bJQueryUI": true
        });
});

    function addToCallAttempts(id) {
    };



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
<table id="specialtyTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
<th>id</th><th>Name</th>
</thead>
<tbody >
  <% if (items) { %>
     <% items.each { %>
      <tr>
        <td>${it.id}</td><td>${it.name}</td>
      </tr>
     <% } %>
   <% } %>
</tbody>
</table>
<form method="post">
  Specialty Type: <input type="text" name="specialtyTypeName"><br>
          <input id="patId" type="hidden" name="patientIdd" value="0">
          <input id="itemId" type="hidden" name="itemId" value="0">
  <input type="submit" value="Submit">
</form>




<div class="Table" id="patientMessageList">
<h2>Active Patient List</h2>
<table id="activeTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
<th>call no answer</th>
<th>specialty</th>
<th>Patient Name</th><th>Contact Attempts</th> <th>Patient Call Date</th>
        <th>Last Contact Attempt Date</th><th>tel</th><th>dashboard</th>
</thead>
<tbody >
  <% if (activePatientListItems) { %>
     <% activePatientListItems.each { %>
      <tr>        
        <td style="cursor:pointer" onclick="addToCallAttempts($it.id)"> <i  class="icon-">&#xf0fe;</i></td>


<td><select onchange="selectSpecialty(this.value,${it.patientId})">
<% items.each { itt->%>

  <% if (itt.id == it.specItem.id) { %>
<option selected value="${itt.id}">${itt.name}</option>
  <% } else { %>

<option value="${itt.id}">${itt.name}</option>
  <% } %>
  <% } %>

</select>
<!-- /${it.specItem.id}/${it.specItem.name} -->
</td>
 

<td>${ ui.format(it.patientId)}</td>
<td>${ ui.format(it.contactAttempts)}</td>
<td>${ ui.format(it.patientCallDate)}</td>
<td>${ ui.format(it.lastContactAttemptDate)}</td>
<td>${ ui.format(it.patientPhone)}</td>
        <td>
        dashboard
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

  </tr>
  <% } %>
</tbody>
</table>

</div>
