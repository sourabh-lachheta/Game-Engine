package inventory;

public class Item {

    private String name;
    private boolean stackable;
    private int healAmount;
    private boolean consumable;
    private ItemType type;
    private int attackDamage;

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
    }

    public Item(String name, boolean stackable, int healAmount, boolean consumable, ItemType type, int attackDamage) {

        this.name = name;
        this.stackable = stackable;
        this.healAmount = healAmount;
        this.consumable = consumable;
        this.type = type;
        this.attackDamage = attackDamage;
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
}
