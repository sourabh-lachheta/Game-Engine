package npc;

public class CombatProfile {

    private int maxHp;
    private int attack;
    private int expReward;

    public CombatProfile(int maxHp, int attack, int expReward){
        this.maxHp = maxHp;
        this.attack = attack;
        this.expReward = expReward;
    }

    public int getAttack(){
        return attack;
    }

    public int getMaxHp(){
        return maxHp;
    }

    public int getExpReward(){
        return expReward;
    }
}
