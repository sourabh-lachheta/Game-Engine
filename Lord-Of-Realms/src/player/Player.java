package player;

import combat.Skill;
import inventory.Inventory;
import inventory.Item;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;

    private int hp;
    private int maxHp;
    private int qi;
    private int maxQi;
    private int exp;
    private int maxExp;
    private int realm;
    private int stage;
    private int attack;
    private int defense;
    private List<Skill> learnedSkills;
    private int gold;

    private Inventory inventory;

    public Player(){

        name = "User";

        maxHp = 100;
        hp = maxHp;

        maxQi = 50;
        qi = maxQi;

        exp = 0;
        maxExp = 50;

        realm = 1;
        stage = 1;

        attack = 10;
        defense = 5;

        gold = 100;

        inventory = new Inventory();
        learnedSkills = new ArrayList<>();

    }

    public String getName(){
        return name;

    }
    public String getHealthText(){
        return hp + "/" + maxHp;
    }

    public String getQiText(){
        return qi + "/" + maxQi;
    }

    public int getExp(){
        return exp;
    }

    public String getExpText(){
        return exp + "/" + maxExp;
    }


    public String getRealmName(){
        switch (realm){
            case 1:
                return "Body Tempering " + stage + "          ";

            case 2:
                return "Qi Gathering " + stage + " ";

            case 3:
                return "Foundation Establishment " + stage + " ";

            default:
                return "Unknown Realm";

        }
    }

    public int getCombatAttack(){
        int realmBonus = 0;

        switch (realm){
            case 1:
                realmBonus = 18;
                break;

            case 2:
                realmBonus = 10;
                break;

            case 3:
                realmBonus = 20;
                break;

        }
        return getAttack() + realmBonus;
    }

    public void takeDamage(int amount){
        amount = amount - getDefense();

        hp -= amount;
        if(hp < 0){
            hp = 0;
        }

    }
    public void heal(int amount){

        hp += amount;
        if(hp > maxHp){
            hp = maxHp;
        }

    }
    public void gainQi(int amount){
        qi += amount;

        if(qi > maxQi){
            qi = maxQi;
        }
    }


    public void consumeQi(int amount){
        qi -= amount;
    }


    public void gainExp(int amount){
        exp += amount;

        if(exp > maxExp){
            exp = maxExp;
        }
    }




    public Inventory getInventory(){
        return inventory;
    }

    public void addItem(Item item){
        inventory.addItem(item);
    }


    public boolean hasItem(Item item){
        return inventory.hasItem(item);
    }


    public boolean removeItem(Item item){
        return inventory.removeItem(item);
    }


    public boolean useItem(Item item){
        if(!hasItem(item)){
            return false;
        }

        if(item.getHealAmount() > 0){
            heal(item.getHealAmount());
        }

        if(item.isConsumable()) {
            removeItem(item);
        }
        return true;
    }


    public boolean isAlive(){
        return hp > 0;
    }


    public int getAttack(){
        return attack;
    }


    public int getDefense(){
        return defense;
    }

    public int getHealth(){
        return hp;
    }

    public boolean learnSkillFromScroll(Item scroll){
        if(!inventory.hasItem(scroll)){
            return false;
        }

        Skill skill = scroll.getSkill();

        if(skill == null){
            return false;
        }

        if(hasSkill(skill)){
            return false;
        }

        learnSkill(skill);

        inventory.removeItem(scroll);

        return true;
    }

    public void learnSkill(Skill skill){
        if(!learnedSkills.contains(skill)){
            learnedSkills.add(skill);
        }
    }

    public boolean hasSkill(Skill skill){
        return learnedSkills.contains(skill);
    }

    public List<Skill> getLearnedSkills(){
        return learnedSkills;
    }

    public int getQi(){
        return qi;
    }

    public int getGold(){
        return gold;
    }

    public void addGold(int amount){
        gold += amount;
    }

    public boolean spendGold(int amount){
        if(gold < amount){
            return false;
        }

        gold -= amount;
        return true;
    }

    public boolean canBreakthrough(){
        return exp >= maxExp && qi >= maxQi;
    }

    public boolean breakthrough(){

        if(!canBreakthrough()){
            return false;
        }

        // Save the old Qi requirement
        int oldMaxQi = maxQi;

        // EXP is completely consumed
        exp = 0;

        if(stage < 9){

            // Keep half of the Qi used for this breakthrough
            qi = oldMaxQi / 2;

            stage++;

            // Requirements for the new stage
            maxExp = 50 + (stage - 1) * 50;
            maxQi = 20 + (stage - 1) * 10;

            // Increase stats
            maxHp += 10;
            hp += 10;

            attack += 2;
            defense += 1;

        } else {

            // Major realm breakthrough
            realm++;
            stage = 1;

            // New realm starts with new requirements
            maxExp = 50;
            maxQi = 20;

            // Major stat increase
            maxHp += 50;


            attack += 10;
            defense += 5;

            // Fully recover after entering a new realm
            hp = maxHp;
            qi = maxQi;
        }

        return true;
    }

}
