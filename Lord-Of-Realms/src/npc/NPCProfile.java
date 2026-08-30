package npc;

public class NPCProfile {

    private String name;
    private String title;
    private String realm;
    private String personality;
    private String description;
    private int talkSceneId;

    public NPCProfile(String name, String title, String realm, String personality, String description, int talkSceneId){
        this.name = name;
        this.title = title;
        this.realm = realm;
        this.personality = personality;
        this.description = description;
        this.talkSceneId = talkSceneId;
    }

    public String getName(){
        return name;
    }

    public String getTitle(){
        return title;
    }

    public String getRealm(){
        return realm;
    }

    public String getPersonality(){
        return personality;
    }

    public String getDescription(){
        return description;
    }

    public int getTalkSceneId(){
        return talkSceneId;
    }
}
