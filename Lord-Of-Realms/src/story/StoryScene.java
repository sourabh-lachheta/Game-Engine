

/*
    -> One scene in the story
    -> Holding the story text and available choices
 */


package story;

import npc.NPC;
import java.util.ArrayList;
import java.util.List;

public class StoryScene {


    private int id;
    private String storyText;
    private List<Choice> choices;
    private List<NPC> npcs;


    public  StoryScene(int id ,String storyText){
        this.id = id;
        this.storyText = storyText;
        this.choices = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    public void addChoice(Choice choice){
        choices.add(choice);
    }

    public int getId(){
        return id;
    }

    public String getStoryText(){
        return storyText;
    }

    public List<Choice> getChoices(){
        return choices;
    }

    public void addNPC(NPC npc){
        npcs.add(npc);
    }

    public List<NPC> getNPCs(){
        return npcs;
    }

}
