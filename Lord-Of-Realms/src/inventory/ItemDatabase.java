package inventory;

public class ItemDatabase {

    public static final Item HEALING_HERB =
            new Item("Healing Herb",true,20,true,ItemType.CONSUMABLE);

    public static final Item RUSTY_SWORD =
            new Item("Rusty Sword",false,0,false,ItemType.WEAPON,10);
}
