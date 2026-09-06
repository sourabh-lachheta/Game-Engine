package npc;

import combat.CombatProfile;
import inventory.ItemDatabase;

public class NPCDatabase {

   public static final NPC WOLF;
   public static final NPC MERCHANT;

   static {

      NPCProfile wolfProfile = new NPCProfile(
              "Wolf",
              "Wild Beast",
              "Qi Gathering",
              "Aggressive",
              "A dangerous wolf that attacks anyone who enters its territory.",
              20
      );

      CombatProfile wolfCombatProfile =
              new CombatProfile(
                      100,   // maxHp
                      15,    // attack
                      5,     // defense
                      50     // expReward
              );

      WOLF = new NPC(
              wolfProfile,
              wolfCombatProfile,
              false
      );



      NPCProfile merchantProfile = new NPCProfile(
              "Merchant",
              "Traveling Merchant",
              "Mortal",
              "Friendly",
              "A Traveling Merchant....",
              30

      );

      MERCHANT = new NPC(
              merchantProfile,
              null,
              true
      );

      MERCHANT.addTradeItem(ItemDatabase.HEALING_HERB,5);
      MERCHANT.addTradeItem(ItemDatabase.RUSTY_SWORD,1);
   }




}
