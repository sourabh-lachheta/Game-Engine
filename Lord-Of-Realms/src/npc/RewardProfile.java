package npc;

import inventory.Item;

import java.util.ArrayList;
import java.util.List;

public class RewardProfile {

    private int expReward;
    private List<Item> itemRewards;

    public RewardProfile(int expReward){
        this.expReward = expReward;
        this.itemRewards = new ArrayList<>();
    }

    public int getExpReward(){
        return expReward;
    }

    public List<Item> getItemReward(){
        return itemRewards;
    }

    public void addItemReward(Item item){
        itemRewards.add(item);
    }

}
