package story;



import javax.swing.text.AsyncBoxView;
import java.util.HashMap;
import java.util.Map;

import inventory.Item;
import inventory.ItemDatabase;
import player.Player;
import combat.CombatProfile;
import combat.Enemy;




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
        // temp
        CombatProfile wolfProfile =
                new CombatProfile(80, 15, 5, 30);

        Enemy wolf =
                new Enemy("Wolf", wolfProfile);



        StoryScene scene1 = new StoryScene(
                1,
                "You slowly open your eyes."
        );

        scene1.addChoice(new Choice("Look Around",2));
        scene1.addChoice(new Choice("Stand up",3));
        scene1.addChoice(new Choice("Check Body",4));
        scene1.addChoice(new Choice("Close Eyes", 5));

        addScene(scene1);



        StoryScene scene2 = new StoryScene(
                2,
                "Towering trees surround you. Nearby, you notice a broken sword half-buried in the soil"
        );
       // Item key = new Item("Key",false);
        scene2.addChoice(new Choice("Pick up the sword",6,ItemDatabase.RUSTY_SWORD,null));
        scene2.addChoice(new Choice("Return",1));
        scene2.addChoice(new Choice("Go Forward", 7));
        addScene(scene2);

        StoryScene scene3 = new StoryScene(
                3,
                "you now can see clearly"
        );
        scene3.addChoice(new Choice("Look Around", 2));
        scene3.addChoice(new Choice("sleep",1));
        addScene(scene3);

        StoryScene scene4 = new StoryScene(
                4,
                "you are totally fine , but on your hand have little mark on it"
        );
        scene4.addChoice(new Choice("touch the mark",1));
        scene4.addChoice(new Choice("Look Around",2));
        addScene(scene4);

        StoryScene scene5 = new StoryScene(
                5,
                "you are sleeping now"
        );
        scene5.addChoice(new Choice("wake up", 1));
        addScene(scene5);

        StoryScene scene6 = new StoryScene(
                6,
                " you have now weapon"

        );
        scene6.addChoice(new Choice("take herb",7,ItemDatabase.HEALING_HERB,null));
        scene6.addChoice(new Choice("Go Forward",7));
        addScene(scene6);


        StoryScene scene7 = new StoryScene(
                7,
                "you see herb"
        );

        scene7.addChoice(new Choice("Take the herb", 9, ItemDatabase.HEALING_HERB,null));
        scene7.addChoice(new Choice("Go Forward", 10 ));
        scene7.addChoice(new Choice("Fight the Wolf", 10, wolf));
        addScene(scene7);

        StoryScene scene9 = new StoryScene(
                9,
                "you have healing herb"
        );

        scene9.addChoice(new Choice("use herb", 11));
        scene9.addChoice(new Choice("Go Forward", 10 ));
        addScene(scene9);


        StoryScene scene10 = new StoryScene(
                10,
                "you defated wolf found another healing item"
        );

        scene10.addChoice(new Choice("Take the herb", 12,ItemDatabase.HEALING_HERB,null));
        scene10.addChoice(new Choice("Go Forward",13));
        addScene(scene10);

        StoryScene scene12 = new StoryScene(
                12,
                " another healing item added"
        );

       // scene10.addChoice(new Choice("Take the herb", 12,herb));
        scene12.addChoice(new Choice("Go Forward",13));
        addScene(scene12);







    }

}
