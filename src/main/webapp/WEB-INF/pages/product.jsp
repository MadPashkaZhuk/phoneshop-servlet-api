<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.entity.Product" scope="request"/>
<jsp:useBean id="latestProducts" scope="request" type="java.util.ArrayDeque"/>
<tags:master pageTitle="Product Page">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">
                There was an error adding to cart
        </div>
    </c:if>
    <p>
        ${product.description}
    </p>

    <form method="post" action="${pageContext.servletContext.contextPath}/products/${product.id}">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>

            </tr>

            <tr>
                <td>Code</td>
                <td>
                    ${product.code}
                </td>
            </tr>

            <tr>
                <td>Stock</td>
                <td>
                    ${product.stock}
                </td>
            </tr>

            <tr>
                <td>Price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>

            <tr>
                <td>quantity</td>
                <td>
                    <input name="quantity" value="${not empty error ? param.quantity : 1}"  class="quantity">
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Add to Cart</button>
        </p>
    </form>

    <c:if test="${not empty latestProducts}">
        <p>Latest items: </p>
        <table>
            <c:forEach var="product" items="${latestProducts}">
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                ${product.description}
                        </a>
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/pricehistory/${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

</tags:master>