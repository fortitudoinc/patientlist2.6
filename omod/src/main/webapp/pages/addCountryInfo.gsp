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
        { label: "Add Country Info"}
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

<h1>Current Countries</h1>
<table id="countryTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
<th>id</th><th>Name</th><th>Code</th>
</thead>
<tbody >
  <% if (countries) { %>
<% countries.each { country->%>
      <tr>
        <td>${country.id}</td><td>${country.name}</td><td>${country.countryCode}</td>
      </tr>
     <% } %>
     <% } %>

</tbody>
</table>
<br/><br/>
<form method="post">
  Add Country: <input type="text" name="countryName" width="28" height="48">
  Country Code (e.g. +234): <input type="text" name="countryCode" width="28" height="48"><br>
  <input type="submit" value="Submit">
</form>






<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>

