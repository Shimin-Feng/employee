<%--
  Created by IntelliJ IDEA.
  User: $himin F
  Date: 3/21/2022
  Time: 2:43 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>student info</h1>
<table>
    <tr>
        <th>number</th>
        <th>name</th>
        <th>age</th>
        <th>operation</th>
    </tr>
    <c:forEach items="${employees}" var="student">
        <tr>
            <td>${student.id}</td>
            <td>${student.name}</td>
            <td>${student.age}</td>
            <td>
                <a href="/hello/findByID/${student.id}">update</a>
                <a href="/hello/deleteByID/${student.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/save.jsp">add student</a>
</body>
</html>
