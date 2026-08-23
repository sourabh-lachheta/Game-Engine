package inventory;

import player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class Inventory {
    private List<InventoryItem> items;
    private Player player;

    public Inventory(){
        items = new ArrayList<>();
    }

    public void addItem(Item item){
        if(item.isStackable()){
            for(InventoryItem inventoryItem : items){
                if(inventoryItem.getItem().equals(item)){
                    inventoryItem.increaseQuantity();
                    return;
                }
            }
        }

        items.add(new InventoryItem(item,1));
    }



    public String getItemsText(){
        if(items.isEmpty()){
            return "Empty";
        }

        StringBuilder text = new StringBuilder();

        for(InventoryItem inventoryItem : items){


            text.append(inventoryItem.getItem().getName())
                    .append(" x ")
                    .append(inventoryItem.getQuantity())
                    .append("\n");
        }

        return text.toString();
    }

    public boolean hasItem(Item item){

        for(InventoryItem inventoryItem : items){

            if(inventoryItem.getItem().equals(item)){
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(Item item){
     Iterator<InventoryItem> iterator = items.iterator();

     while(iterator.hasNext()){
         InventoryItem inventoryItem = iterator.next();

         if(inventoryItem.getItem().equals(item)){
             if(inventoryItem.getQuantity() > 1){
                 inventoryItem.decreaseQuantity();
             }else{
                 iterator.remove();
             }

             return true;
         }
     }
     return false;
    }


    public List<InventoryItem> getItems(){
        return new ArrayList<>(items);
    }

}
