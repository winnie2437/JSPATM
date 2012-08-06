package webatm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddToBasketServlet
 */
@WebServlet
(
		name = "addToBasketServlet",
		urlPatterns = {"/addToBasket.html"}
)


public class AddToBasketServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//private static final List<String> sList = new ArrayList<String>();
	private static final HttpShoppingBasket sBasket = new HttpShoppingBasket();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToBasketServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{ 
		HttpSession session = request.getSession(true);
		
		//session.invalidate();
		
		//HttpShoppingBasket basket = (HttpShoppingBasket)session.getAttribute("SHOPPING_BASKET");
		//if (basket == null) basket = new HttpShoppingBasket();
		HttpShoppingBasket basket = sBasket;
		
		String productName = request.getParameter("productName"); 
		double price = Double.parseDouble(request.getParameter("price"));
		Item item = new Item(productName, price);
		basket.addToBasket(item); 
		session.setAttribute("SHOPPING_BASKET", basket); 
		response.sendRedirect("/WebAtm/jsp/products.jsp"); 
	}
}
