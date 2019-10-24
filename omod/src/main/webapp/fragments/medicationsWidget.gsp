<div class="info-section">
    <div class="info-header">
        <i class="icon-diagnosis"></i>
        <h3>MEDICATIONS</h3>
<i id ="medications-editMedicationHistory" class="icon-pencil edit-action right" title="Edit" 
onclick="location.href='${link11}';"> </i>
    </div>
    <div class="info-body">
<h2>Medication History</h2>
   <% if (pastMedsHistory) { %>   
     <% pastMedsHistory.each { %>
${it}</br>
     <% } } else { %>
None
    <% } %>
<h2>Fortitudo Drug Orders</h2>
   <% if (drugOrders) { %>   
     <% drugOrders.each { %>
${it}</br>
     <% } } else { %>
None
    <% } %>
    </div>
</div>
