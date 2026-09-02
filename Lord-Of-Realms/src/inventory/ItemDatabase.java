package inventory;

import combat.Skill;

public class ItemDatabase {

    public static final Item HEALING_HERB =
            new Item("Healing Herb",true,20,true,ItemType.CONSUMABLE,0,10);

    public static final Item RUSTY_SWORD =
            new Item("Rusty Sword",false,0,false,ItemType.WEAPON,10,50);

    public static final Skill FIRE_BALL =
            new Skill("Fire Ball", 30,30);

    public static final Item FIRE_SCROLL =
            new Item("Ancient Fire Scroll", false,FIRE_BALL,ItemType.SKILL);
}
