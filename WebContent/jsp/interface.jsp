<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>INITIAL SCREEN</title>
</head>
<body>

<div class="initial">
  <b>Welcome to JavaTC ATM</b> <br/>
  <b>Insert your card:</b>  
  
  <table>

    <tr>
      <td>CARD: </td>
      <td>
        <form action="/WebAtm/controller.html" method="POST">
          Card Number: <input type="text" name="cardnumber" />
          PIN: <input type="text" name="pin" />
          <input type="submit" value="Insert" />
          <input type="hidden" name="command" value="cardin"/>          
        </form>
      </td>
    </tr>

    <tr>
      <td>SHOW BALANCES:</td>
      <td>
        <form action="/WebAtm/controller.html" method="POST">
          <input type="submit" value="Run" /> 
          <input type="hidden" name="command" value="balances"/>          
        </form>
      </td>
    </tr>

  </table>
</div>

<c:set var="output" value="${sessionScope.OUTPUT}"/> 
  <c:if test="${output != null && not empty output}"> 
  <b>ATM shows:</b> <br/>
  <table>  

    <c:forEach items="${output}" var="line"> 
      <tr>
        <td><c:out value="${line}"/></td>
      </tr>
    </c:forEach>
  </table>
  </c:if>
  
  <c:if test="${output == null || empty output }"> 
    <b>nothing yet ... </b>
  </c:if>  


</body>
</html>