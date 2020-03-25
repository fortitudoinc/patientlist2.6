
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
        { label: "Broadcast Message"}
    ];
</script>

<script>
function selectCountry(country) {
    if (country == "0") {
        alert("Select a country");
        }
}
</script>

<form method="post">
<h1>Broadcast Message to Patients</h1> <br><br>
<label for="country">Select Country:</label>
<select id="country" name="country" onchange="selectCountry(this.value)">
    <option selected value="0">select a country</option>
    <% countries.each { country->%>
    <option value="${country}">${country}</option>
     <% } %>
</select>

<br><br>
<label for="message">Text Message:</label>

<textarea id="message" rows="4" cols="50" name="message">

</textarea>
<br><br>

  <input type="submit" value="Submit">
</form>

<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>

