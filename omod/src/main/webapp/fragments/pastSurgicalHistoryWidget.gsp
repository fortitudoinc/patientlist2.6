<div class="info-section">
    <div class="info-header">
        <i class="icon-diagnosis"></i>
        <h3>PAST SURGICAL HISTORY</h3>
<i id ="medications-editSurgicalHistory" class="icon-pencil edit-action right" title="Edit" 
onclick="location.href='${link1}';"> </i>
    </div>
    <div class="info-body">
  <% if (allHistory1) { %>   
     <% allHistory1.each { %>
${it}</br>
     <% } } else { %>
None
    <% } %>
    </div>
</div>
