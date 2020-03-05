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
        { label: "Personnel Profile"}
    ];
</script>

<script>

var countries;

    function selectPersonnel(userIdTelno) {
            var p = userIdTelno.split("/");
            document.getElementById('userId').value = p[0];
            document.getElementById('currentTelNo').value = p[1];

           jq.each(jq("input[name='country_check']"), function(){
                if (countryInUserList(jq(this).val(),p[2]))  {
                    jq(this).prop('checked', true);
                }
            });
   }
    function countryInUserList(country, personnelCountries) {
        if (personnelCountries.includes(country)) {
            return true;
            }
        return false;
    }
 
    function getCountriesForUser() {
            var userCountries = "";
            jq.each(jq("input[name='country_check']:checked"), function(){
                if (userCountries == "") {
                    userCountries = jq(this).val();
                    } else {
                    userCountries = userCountries + "," + jq(this).val();
                    }
            });
            document.getElementById('personnelCountries').value = userCountries;
            return userCountries;
    }

function isValidSubmit() {
    var countries = getCountriesForUser();
    if ( (document.getElementById('userId').value == 0) ||
         ( (document.getElementById('telNo').value == 0) &&
            (document.getElementById('currentTelNo') == "") ) ||
         (countries == "") ) {
             alert("Please select user and provide telephone number and country(ies)");
             return false;
         } else {
             return true;
         }
}
</script>

<div>
<form method="post">

<table style="width:100%">
<tr> 
<td>
<% if (personnel) { %>   
   <select onchange="selectPersonnel(this.value)">
   <option value="0">Select Personnel</option>
    <% personnel.each { personel->%>
     <option value="${personel.userId}/${personel.telno}/${personel.countries}">${personel.givenName + " " + personel.familyName} </option>
     <% } %>
    </select>
     <% } %>

</td>
<td>Current telephone number: <input type="text" id="currentTelNo" value="" size="35"></td>
</tr>
</table>


<br><br>
Add/Update Personnel Phone Number (include country code; e.g. +25408145663444): <input id="telNo" type="text" name="telNo">
<input id="userId" type="hidden" value="0" name="userId">

<br><br>
Select Countries Where Assisting<br><br>
<% allCountries.each { country->%>
    <input type="checkbox" name="country_check" value="${country}">${country}<br>  
<% } %>

<input id="personnelCountries" type="hidden" name="personnelCountries" value="0">


<input type="submit" value="Submit" onclick='return isValidSubmit();'>
</form>
</div>

</head>
<body>
 

<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>

