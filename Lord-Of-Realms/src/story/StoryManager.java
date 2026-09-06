package story;



import javax.swing.text.AsyncBoxView;
import java.util.HashMap;
import java.util.Map;

import inventory.Item;
import inventory.ItemDatabase;
import npc.NPCDatabase;
import player.Player;
import combat.CombatProfile;
import combat.Enemy;
import story.mortalRealm.MortalRealmStory;


public class StoryManager {

    private Map<Integer, StoryScene> scenes;
    private StoryScene currentScene;
    private Player player;
    private String lastMessage;

    public StoryManager(Player player){
        scenes = new HashMap<>();
        this.player= player;
        initializeStory();
    }

    public void addScene(StoryScene scene){
        scenes.put(scene.getId(),scene);
    }

    public void startStory(int startSceneId){
        currentScene = scenes.get(startSceneId);

    }

    public boolean selectChoice(int choiceIndex){
        Choice selectedChoice = currentScene.getChoices().get(choiceIndex);

        Item requiredItem = selectedChoice.getRequiredItem();

        if(requiredItem != null && !player.hasItem(requiredItem)){
            lastMessage = "you need : " + requiredItem.getName();
            return false;
        }

        Item reward = selectedChoice.getReward();

        if(reward != null){
            player.addItem(reward);
        }

        int nextSceneId = selectedChoice.getNextSceneId();
        goToScene(nextSceneId);

        return true;

    }

    public String getLastMessage(){
        return lastMessage;
    }

    public StoryScene getCurrentScene(){
        return currentScene;

    }

    private void goToScene(int sceneId){
        currentScene = scenes.get(sceneId);

    }

    public void completeCombat(int nextSceneId){
        goToScene(nextSceneId);
    }

    private void initializeStory(){
        MortalRealmStory.load(this);
    }

}
