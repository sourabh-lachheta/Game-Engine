
package npc;

import combat.CombatProfile;
import inventory.InventoryItem;
import inventory.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NPC {

    private NPCProfile profile;
    private CombatProfile combatProfile;
    private boolean canTrade;
    private List<InventoryItem> tradeItems;

    public NPC(NPCProfile profile, CombatProfile combatProfile, boolean canTrade) {
        this.profile = profile;
        this.combatProfile = combatProfile;
        this.canTrade = canTrade;

        tradeItems = new ArrayList<>();
    }

    public NPCProfile getProfile() {
        return profile;
    }

    public CombatProfile getCombatProfile() {
        return combatProfile;
    }

    public boolean canFight(){
        return combatProfile != null;
    }

    public boolean canTrade(){
        return canTrade;
    }

    public void addTradeItem(Item item, int quantity){
        tradeItems.add(new InventoryItem(item, quantity));
    }

    public List<InventoryItem> getTradeItems(){
        return new ArrayList<>(tradeItems);
    }

    public boolean buyItem(Item item) {

        Iterator<InventoryItem> iterator = tradeItems.iterator();

        while (iterator.hasNext()) {

            InventoryItem inventoryItem = iterator.next();

            if (inventoryItem.getItem().equals(item)) {

                if (inventoryItem.getQuantity() > 1) {
                    inventoryItem.decreaseQuantity();
                } else {
                    iterator.remove();
                }

                return true;
            }
        }

        return false;
    }
}