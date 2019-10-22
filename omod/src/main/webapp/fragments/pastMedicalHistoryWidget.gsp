


<div class="info-section">
    <div class="info-header">
        <i class="icon-diagnosis"></i>
        <h3>PAST MEDICAL HISTORY</h3>
<i id ="medications-editMedicalHistory" class="icon-pencil edit-action right" title="Edit" 
onclick="location.href='${link}';"> </i>
    </div>
    <div class="info-body">
     <% allHistory.each { %>
${it}</br>
     <% } %>

    </div>
</div>
