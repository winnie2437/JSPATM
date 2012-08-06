package webatm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HttpShoppingBasket implements Serializable
{
  private List<Item> items = new ArrayList<Item>();
  private double totalValue = 0;
  
  public void addToBasket(Item item)
  {
    this.items.add(item);
    this.totalValue+=item.getPrice();
  }
  
  public List<Item> getItems() 
  {
    return items;
  }
  
  public double getTotalValue() 
  {
    return totalValue;
  }
}
