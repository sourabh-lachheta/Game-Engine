package inventory;

import combat.Skill;

public class Item {

    private String name;
    private boolean stackable;
    private int healAmount;
    private boolean consumable;
    private ItemType type;
    private int attackDamage;
    private Skill skill;
    private int price;

    public Item(String name, boolean stackable){
        // this.name = name;
        //this.stackable = stackable;

        this(name, stackable, 0,false,ItemType.KEY,0); //temporarily put ItemType.KEY
    }

    public Item(String name, boolean stackable, int healAmount, boolean consumable, ItemType type){
        this.name = name;
        this.stackable = stackable;
        this.healAmount = healAmount;
        this.consumable = consumable;
        this.type = type;

        this.price = 0;
    }

    public Item(String name, boolean stackable, int healAmount, boolean consumable, ItemType type, int attackDamage) {

        this.name = name;
        this.stackable = stackable;
        this.healAmount = healAmount;
        this.consumable = consumable;
        this.type = type;
        this.attackDamage = attackDamage;

        this.price = 0;
    }

    public Item(String name, Boolean stackable, Skill skill,ItemType type){
        this.name = name;
        this.stackable = stackable;
        this.healAmount = 0;
        this.consumable = true;
        this.type = ItemType.SKILL;
        this.skill = skill;

        this.price = 0;
    }

    public Item(String name, boolean stackable, int healAmount,
                boolean consumable, ItemType type, int attackDamage,
                int price) {

        this.name = name;
        this.stackable = stackable;
        this.healAmount = healAmount;
        this.consumable = consumable;
        this.type = type;
        this.attackDamage = attackDamage;
        this.price = price;
    }

    public Skill getSkill(){
        return skill;
    }

    public int getAttackDamage(){
        return attackDamage;
    }

    public ItemType getType(){
        return type;
    }

    public boolean isConsumable(){
        return consumable;
    }

    public int getHealAmount(){
        return healAmount;
    }

    public String getName(){
        return name;
    }


    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Item)){
            return false;
        }

        Item other = (Item) obj;

        return name.equals(other.name) && stackable == other.stackable;
    }


    @Override
    public int hashCode(){
        return 31 *  name.hashCode() + Boolean.hashCode(stackable);
    }

    public boolean isStackable(){
        return  stackable;
    }

    public int getPrice(){
        return price;
    }
}
