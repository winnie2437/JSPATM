<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CLIENT SCREEN</title>
</head>
<body>

<div class="client">
  <b>Welcome to JavaTC ATM</b> <br/>
  <b>Select your command:</b>  
  
  <table>

    <tr>
      <td>WITHDRAWAL: </td>
      <td>
        <form action="/WebAtm/controller.html" method="POST">
          Currency: <input type="text" name="currency" />
          Amount: <input type="text" name="amount" />
          <input type="submit" value="Run" />
          <input type="hidden" name="command" value="minus"/>          
        </form>
      </td>
    </tr>

    <tr>
      <td>DEPOSIT:</td>
      <td>
        <form action="/WebAtm/controller.html" method="POST">
          Currency: <input type="text" name="currency" />
          Nominal: <input type="text" name="nominal" />
          Qty: <input type="text" name="qty" />          
          <input type="submit" value="Run" /> 
          <input type="hidden" name="command" value="plus"/>          
        </form>
      </td>
    </tr>

    <tr>
      <td>REGISTER NEW ACCOUNT:</td>
      <td>
        <form action="/WebAtm/controller.html" method="POST">
          Currency: <input type="text" name="currency" />
          <input type="submit" value="Run" /> 
          <input type="hidden" name="command" value="register"/>          
        </form>
      </td>
    </tr>

    <tr>
      <td>DELETE ACCOUNT:</td>
      <td>
        <form action="/WebAtm/controller.html" method="POST">
          Account Number: <input type="text" name="account" />
          <input type="submit" value="Run" /> 
          <input type="hidden" name="command" value="delete"/>          
        </form>
      </td>
    </tr>

    <tr>
      <td>TRANSFER:</td>
      <td>
        <form action="/WebAtm/controller.html" method="POST">
          Debit: <input type="text" name="accountfrom" />
          Credit: <input type="text" name="accountto" />          
          Amount: <input type="text" name="amount" />          
          <input type="submit" value="Run" /> 
          <input type="hidden" name="command" value="transfer"/>          
        </form>
      </td>
    </tr>

    <tr>
      <td>
        <form action="/WebAtm/controller.html" method="POST">
          <input type="submit" value="Show Balance" /> 
          <input type="hidden" name="command" value="balance"/>          
        </form>
      </td>

      <td>
        <form action="/WebAtm/controller.html" method="POST">
          <input type="submit" value="List Accounts" /> 
          <input type="hidden" name="command" value="accounts"/>          
        </form>
      </td>


      <td>
        <form action="/WebAtm/controller.html" method="POST">
          <input type="submit" value="Show Card Transactions" /> 
          <input type="hidden" name="command" value="cardtransactions"/>          
        </form>
      </td>

      <td>
        <form action="/WebAtm/controller.html" method="POST">
          <input type="submit" value="Show Client Transactions" /> 
          <input type="hidden" name="command" value="clienttransactions"/>          
        </form>
      </td>

      <td>
        <form action="/WebAtm/controller.html" method="POST">
          <input type="submit" value="Cancel" /> 
          <input type="hidden" name="command" value="cancel"/>          
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