<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Apress Demo Store</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head>
<body>
<div class="content">
  <b>Welcome to Apress store </b> <br/> <br/>
  <b>Select product:</b>  <br/>
  
  <table>
  
    <tr>
      <td>Product Name</td>
      <td>Price</td>
      <td>Actions</td>
    </tr>
    
    <tr>
      <td>Apache Tomcat 7</td>
      <td>$34.99</td>
      <td>
        <form action="/WebAtm/addToBasket.html" method="POST">
          <input type="hidden" name="productName" value="Apache Tomcat 7"/> 
          <input type="hidden" name="price" value="34.99"/> 
          <input type="submit" value="Add to basket" /> 
        </form>
      </td>
    </tr>
    
    <tr>
      <td>Pro Spring 3</td>
      <td>$39.99</td>
      <td>
        <form action="/WebAtm/addToBasket.html" method="POST">
          <input type="hidden" name="productName" value="Pro Spring 3"/>
          <input type="hidden" name="price" value="39.99"/>
          <input type="submit" value="Add to basket" />
        </form>
      </td>
    </tr>
    
    <tr>
      <td>Android Development for Beginners</td>
      <td>$24.99</td>
      <td>
        <form action="/WebAtm/addToBasket.html" method="POST">
          <input type="hidden" name="productName" value="Android Development for Beginners"/>
          <input type="hidden" name="price" value="24.99"/>
          <input type="submit" value="Add to basket" />
        </form>
      </td>
    </tr>
    
  </table>
</div>

<c:set var="basket" value="${sessionScope.SHOPPING_BASKET}"/> 
  <c:if test="${basket != null && not empty basket.items}"> 
  <b>Your shopping cart:</b> <br/>
  <table>  
    <tr>
      <td><b>Product</b></td>
      <td><b>Price</b></td>
    </tr>

    <c:forEach items="${basket.items}" var="item"> 
      <tr>
        <td><c:out value="${item.name}"/></td>
        <td><c:out value="${item.price}"/></td> 
      </tr>
    </c:forEach>
  </table>
  
  <tr>
    <td><b>Total:</b></td>
    <td> $<c:out value="${basket.totalValue}"/>  </td>
  </tr>
  </c:if>
  
  <c:if test="${basket ==null || empty basket.items }"> 
    <b>Your basket is empty</b>
  </c:if>  



  
</body>
</html>