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
        { label: "Add Specialty Type"}
    ];
</script>


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
});
</script>

<h1>Current Specialties</h1>
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
<br/><br/>
<form method="post">
  Add Specialty Type: <input type="text" name="specialtyTypeName"><br>
  <input type="submit" value="Submit">
</form>






<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>
