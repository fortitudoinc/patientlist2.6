<div class="info-section">
    <div class="info-header">
        <i class="icon-diagnosis"></i>
        <h3>MEDICATIONS</h3>
<i id ="medications-editMedicationHistory" class="icon-pencil edit-action right" title="Edit" 
onclick="location.href='${link11}';"> </i>
    </div>
    <div class="info-body">
   <% if (allHistory11) { %>   
     <% allHistory11.each { %>
${it}</br>
     <% } } else { %>
None
    <% } %>

    </div>
</div>
