
package npc;

import combat.CombatProfile;

public class NPC {

    private NPCProfile profile;
    private CombatProfile combatProfile;

    public NPC(NPCProfile profile, CombatProfile combatProfile) {
        this.profile = profile;
        this.combatProfile = combatProfile;
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
}