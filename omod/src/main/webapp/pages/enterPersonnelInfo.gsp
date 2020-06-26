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
                //userIdTelno is personel.userId/personel.telno/personel.personCountriesIds
            var p = userIdTelno.split("/");
            document.getElementById('userId').value = p[0];
            document.getElementById('currentTelNo').value = p[1];

           jq.each(jq("input[name='country_check']"), function(){
                if (countryInUserList(jq(this).val(),p[2]))  {
                    jq(this).prop('checked', true);
                }
            });
   }
    function countryInUserList(countryId, personCountriesIds) {
        var idp = personCountriesIds.replace("[",",");
        idp = idp.replace("]",",");
        idp = idp.replace(/ /g, "");
        if (idp.includes("," + countryId + ",") ) {
            return true;
        }
        return false;
}
 
    function getCountryIdsForUser() {
            var userCountryIds = "";
            jq.each(jq("input[name='country_check']:checked"), function(){
                if (userCountryIds == "") {
                    userCountryIds = jq(this).val();
                    } else {
                    userCountryIds = userCountryIds + "," + jq(this).val();
                    }
            });
            document.getElementById('personnelCountryIds').value = userCountryIds;
            return userCountryIds;
    }

function isValidSubmit() {
    var userCountryIds = getCountryIdsForUser();
    if ( (document.getElementById('userId').value == 0) ||
         ( (document.getElementById('telNo').value == 0) &&
            (document.getElementById('currentTelNo') == "") ) ||
         (userCountryIds == "") ) {
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
     <option value="${personel.userId}/${personel.telno}/${personel.personCountriesIds}">${personel.givenName + " " + personel.familyName} </option>
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
    <input type="checkbox" name="country_check" value="${country.id}">${country.name}<br>  
<% } %>

<input id="personnelCountryIds" type="hidden" name="personnelCountryIds" value="0">


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

