package webatm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import monetary.Currency;
import monetary.Nominal;
import terminal.AtmCashIn;
import terminal.AtmException;
import terminal.CashOperationException;
import terminal.CashOutCassette;
import terminal.CashSlice;
import terminal.Cassette;
import terminal.RecycleCassette;
import bank.TheBank;

/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet
(
		name = "controllerServlet",
		urlPatterns = {"/controller.html"}
)
public class ControllerServlet extends HttpServlet  
{
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControllerServlet() 
    {
        super();
    }

    public void init(ServletConfig vConfig) throws ServletException
    {
    	super.init(vConfig);
    	
    	AtmCashIn lATM = null;    	
    	
		try
		{
			TheBank lBank = TheBank.getBank();
			
	    	lATM = new AtmCashIn(9,lBank);
			ArrayList<Cassette> lCassettes = new ArrayList<Cassette>();
			for (int i = 0; i < 8; i++ ) lCassettes.add(new CashOutCassette(new CashSlice(Currency.UAH,Nominal.values()[i],2)));    	
	        //lCassettes.add(new CashInCassette(new CashSlice(null,null,0)));
			//lCassettes.add(new CashInCassette(new CashSlice(Currency.UAH,Nominal.ONE_HUNDRED,0)));
			lCassettes.add(new RecycleCassette(new CashSlice(Currency.UAH,Nominal.ONE_HUNDRED,0)));
			
			lATM.load(lCassettes);
			
						
		}
		catch (AtmException e)
		{
			e.printStackTrace();
		}
		
		vConfig.getServletContext().setAttribute("ATM", lATM);		
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException 
	{
		HttpSession lSession = pRequest.getSession();
		if (lSession != null)
		{
			lSession.invalidate();
			pResponse.sendRedirect("/WebAtm/jsp/interface.jsp");
		}
	}

	
	private Currency getCurrency(HttpServletRequest vRequest)
	{
		Currency lCurrency = null;
		try 
		{
			lCurrency = Currency.valueOf(vRequest.getParameter("currency"));
		}
		catch (Exception e) 
		{
			((List<String>)vRequest.getSession().getAttribute("OUTPUT")).add("unknown currency, please try again");
		}
		return lCurrency;
	}
	

	protected void clientLoop(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException  
	{
		HttpSession lSession = pRequest.getSession(true);
		List<String> lOutput = (List<String>)lSession.getAttribute("OUTPUT");

    	AtmCashIn lATM = (AtmCashIn)getServletContext().getAttribute("ATM");
		String lCommand = pRequest.getParameter("command");
		Currency lCurrency = null;
		
		switch (lCommand)
		{
		    case "register":
		    	if ( (lCurrency = getCurrency(pRequest)) == null ) break;
		    	lOutput.add(lATM.registerAccount(lCurrency));
		    	break;
		    	
		    case "delete":
		    	int lAccountToDelete = 0;	
		    	try 
		    	{
		    		lAccountToDelete = Integer.parseInt(pRequest.getParameter("account"));
		    	}
				catch (Exception e) 
				{
					lOutput.add("incorrect account number, please try again");
					break;
				}
		    	
		    	lOutput.add(lATM.deleteAccount(lAccountToDelete));
		    	break;
		    	
		    case "transfer":
		    	int lAccountFrom = 0;	
		    	try 
		    	{
		    		lAccountFrom = Integer.parseInt(pRequest.getParameter("accountfrom"));
		    	}
				catch (Exception e) 
				{
					lOutput.add("incorrect debit account number, please try again");
					break;
				}

		    	int lAccountTo = 0;	
		    	try 
		    	{
		    		lAccountTo = Integer.parseInt(pRequest.getParameter("accountto"));
		    	}
				catch (Exception e) 
				{
					lOutput.add("incorrect credit account number, please try again");
					break;
				}
		    	
			    int lAmountForTransfer = 0;
			    try 
			    {
			    	lAmountForTransfer = Math.round(Float.parseFloat(pRequest.getParameter("amount")) * 100);
			    }
				catch (Exception e) 
				{
					lOutput.add("incorrect amount for transfer, please try again");
					break;
				}
			    lOutput.add(lATM.moneyTransfer(lAccountFrom,lAccountTo,lAmountForTransfer));
		    	break;
		    	
		    case "minus":
			    if ( (lCurrency = getCurrency(pRequest)) == null ) break;
			    int lAmount = 0;
			    try 
			    {
			    	lAmount = Integer.parseInt(pRequest.getParameter("amount"));
			    }
				catch (Exception e) 
				{
					lOutput.add("incorrect amount for withdrawal, please try again");
					break;
				}
				
		    	List<CashSlice> lBundle = new ArrayList<CashSlice>();				
				try
				{
					if (!lATM.withdrawalTransaction(lCurrency,lAmount,lBundle)) break;					
				}
				catch (CashOperationException e)
				{
					lOutput.add("ERROR : " + e.getMessage());					
					break;
				}
				catch (AtmException a)
				{
					lOutput.add("ATM_ERROR : " + a.getMessage());					
					break;
				}
				
		    	for (CashSlice cs : lBundle) lOutput.add(String.format("%d %d",cs.getNominalValue(),cs.getQty()));
		    	lOutput.add("WITHDRAWAL OK");
		    	if (!lATM.getCardReader().isAuthorized())
		    	{
	    			lSession.setAttribute("CARD", null);		    		
		    		lOutput.add("CARD OUT");
					pResponse.sendRedirect("/WebAtm/jsp/interface.jsp");		    	
			    	return;
		    	}
		    	break;
		    	
		    case "plus":
			    if ( (lCurrency = getCurrency(pRequest)) == null ) break;
				Nominal lNominal = null;
				try 
				{
					lNominal = Nominal.enumOf(Integer.parseInt(pRequest.getParameter("nominal")));
				}
				catch (Exception e) 
				{
					lOutput.add("unknown banknote, please try again");
					break;					
				}

		    	int lQty = 0;
				try 
				{
					lQty = Integer.parseInt(pRequest.getParameter("qty"));
				}
				catch (Exception e) 
				{
					lOutput.add("incorrect qty for deposit, please try again");
					break;					
				}
				
				CashSlice lSlice = new CashSlice(lCurrency,lNominal,lQty);
				try
				{
					if (!lATM.depositTransaction(lSlice))
					{
						lOutput.add("REJECTED: " + lCurrency + " "  + lNominal + " " + lQty);
						break;							
					}
				}
				catch (CashOperationException e)
				{
					lOutput.add("ERROR : " + e.getMessage());					
					break;
				}
				catch (AtmException a)
				{
					lOutput.add("ATM_ERROR : " + a.getMessage());					
					break;
				}
				
		    	lOutput.add("DEPOSIT OK");
		    	if (!lATM.getCardReader().isAuthorized())
		    	{
	    			lSession.setAttribute("CARD", null);		    		
		    		lOutput.add("CARD OUT");
					pResponse.sendRedirect("/WebAtm/jsp/interface.jsp");		    	
			    	return;
		    	}
		    	break;

		    case "balance":
		    	if (lATM.getAccountAuthorized() == null)
		    	{
		    		lOutput.add("client not authenticated");
		    		break;
		    	}
		    	lOutput.add(String.format("CARD BALANCE: %s %10.2f",lATM.getAccountAuthorized().currency,((double)lATM.getAccountAuthorized().balance)/100));
		    	break;

		    case "accounts":
			    List<String> lAccountsInfo = lATM.getAccountsInfoList();
			    if (lAccountsInfo == null)
			    {
			    	lOutput.add("no accounts");
			    	break;
			    }
		    	for (String str : lAccountsInfo) lOutput.add(str);
			    break;
		    	
		    case "cardtransactions":
			    List<String> lTransactions = lATM.getCardTransactions();
			    if (lTransactions == null)
			    {
			    	lOutput.add("empty list");
			    	break;
			    }
		    	for (String str : lTransactions) lOutput.add(str);
			    break;

		    case "clienttransactions":
			    List<String> lAllTransactions = lATM.getClientTransactions();
			    if (lAllTransactions == null)
			    {
			    	lOutput.add("empty list");
			    	break;
			    }
		    	for (String str : lAllTransactions) lOutput.add(str);
			    break;
		    	
		    case "cancel":
		    	//if (lATM.getCardReader().isAuthorized())
		    	{
	    			lSession.setAttribute("CARD", null);		    		
		    		lATM.getCardReader().cardOut();
		    		lOutput.add("CARD OUT");
					pResponse.sendRedirect("/WebAtm/jsp/interface.jsp");		    	
			    	return;
		    	}
		    	//break;		    	
		    	
		    default: lOutput.add("unknown command, please try again"); 		    	
		}
		
		pResponse.sendRedirect("/WebAtm/jsp/client_interface.jsp"); 
	}
	

	
	protected void initialLoop(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException  
	{
		HttpSession lSession = pRequest.getSession(true);
		
		List<String> lOutput = (List<String>)lSession.getAttribute("OUTPUT");
		if (lOutput == null)
		{
			lOutput = new ArrayList<String>();			
			lSession.setAttribute("OUTPUT", lOutput);
		}

    	AtmCashIn lATM = (AtmCashIn)getServletContext().getAttribute("ATM");
		String lCommand = pRequest.getParameter("command");
		
		switch (lCommand)
		{
            case "cardin":
	    		if (lATM.getCardReader().cardIn(pRequest.getParameter("cardnumber"),pRequest.getParameter("pin")))
	    		{
	    			lSession.setAttribute("CARD", pRequest.getParameter("cardnumber"));
	    			lOutput.add("CARD ACCEPTED");
					pResponse.sendRedirect("/WebAtm/jsp/client_interface.jsp");		    	
			    	return;
	    		}
	    		else lOutput.add("CARD REJECTED");  
		        break;
		        
		    case "balances":
			    List<CashSlice> lBalances = lATM.getAtmBalances();
			    if (lBalances == null)
			    {
			    	lOutput.add("ATM is in unloaded state");
			    }
			    else 
			    {
			    	for (CashSlice cs : lBalances)	lOutput.add(String.format("%s %d %d",cs.getCurrency(),cs.getNominalValue(),cs.getQty()));
			    }
			    lOutput.add("REJECT: " + lATM.getRejectBoxState());
			    lOutput.add("BALANCES OK");
			    break;
			    
		    default: lOutput.add("unknown or wrong command, please try again"); 		    	
		    	
		}
		
		pResponse.sendRedirect("/WebAtm/jsp/interface.jsp"); 
	}
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException  
	{
		//getServletConfig().getServletContext().
		
		HttpSession lSession = pRequest.getSession(true);
		
		String lCard = (String)lSession.getAttribute("CARD");
		if (lCard == null) initialLoop(pRequest, pResponse);
		else clientLoop(pRequest, pResponse);
	}

}
