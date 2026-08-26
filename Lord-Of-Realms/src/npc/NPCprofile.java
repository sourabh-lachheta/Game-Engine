package npc;

public class NPCprofile {

    private String name;
    private String title;
    private String realm;
    private String personality;
    private String description;

    public NPCprofile(String name, String title, String realm, String personality, String description){
        this.name = name;
        this.title = title;
        this.realm = realm;
        this.personality = personality;
        this.description = description;
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
}
