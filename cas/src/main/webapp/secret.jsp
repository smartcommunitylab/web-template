<%@page import="org.springframework.security.core.GrantedAuthority"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<html>
<body>

<p> I know you !!</p>

<%
for(GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
%>
<p><%=ga.getAuthority() %></p>
<%} %>
</body>
</html>