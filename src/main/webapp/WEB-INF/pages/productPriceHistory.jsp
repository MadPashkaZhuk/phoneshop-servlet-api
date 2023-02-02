<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.entity.Product" scope="request"/>
<tags:master pageTitle="Product Price History">
  <h1>${product.description} price history</h1>

  <c:forEach var="priceHistory" items="${product.priceHistoryList}">
    Date: <c:out value="${priceHistory.date}"/>  Price: <c:out value="${priceHistory.price}}"/> <br />

  </c:forEach>
</tags:master>