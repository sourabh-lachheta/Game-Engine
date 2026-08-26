package combat;

public class Skill {

    private String name;
    private int damage;
    private int qiCost;

    public Skill(String name, int damage, int qiCost){
        this.name = name;
        this.damage = damage;
        this.qiCost = qiCost;
    }

    public String getName(){
        return name;
    }

    public int getDamage(){
        return damage;
    }

    public int getQiCost(){
        return qiCost;
    }
}
