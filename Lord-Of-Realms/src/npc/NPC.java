
package npc;

import combat.CombatProfile;

public class NPC {

    private NPCProfile profile;
    private CombatProfile combatProfile;
    private boolean canTrade;

    public NPC(NPCProfile profile, CombatProfile combatProfile, boolean canTrade) {
        this.profile = profile;
        this.combatProfile = combatProfile;
        this.canTrade = canTrade;
    }

    public NPCProfile getProfile() {
        return profile;
    }

    public CombatProfile getCombatProfile() {
        return combatProfile;
    }

    public boolean canFight(){
        return combatProfile != null;
    }

    public boolean canTrade(){
        return canTrade;
    }
}