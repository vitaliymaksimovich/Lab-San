package model;

import java.util.ArrayList;
import java.util.List;

public class Monster {
    private String name;
    private String description;
    private int dangerLevel;
    private List<String> habitats;
    private String firstMention;
    private List<String> immunities;
    private String activityTime;
    private String poisonRecipe;
    private int preparationTime;
    private String effectiveness;
    private double height;
    private double weight;
    private String source;

    public Monster() {
        habitats = new ArrayList<>();
        immunities = new ArrayList<>();
    }

    // Fuck this shit
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDangerLevel() { return dangerLevel; }
    public void setDangerLevel(int dangerLevel) { this.dangerLevel = dangerLevel; }
    public List<String> getHabitats() { return habitats; }
    public void setHabitats(List<String> habitats) { this.habitats = habitats; }
    public String getFirstMention() { return firstMention; }
    public void setFirstMention(String firstMention) { this.firstMention = firstMention; }
    public List<String> getImmunities() { return immunities; }
    public void setImmunities(List<String> immunities) { this.immunities = immunities; }
    public String getActivityTime() { return activityTime; }
    public void setActivityTime(String activityTime) { this.activityTime = activityTime; }
    public String getPoisonRecipe() { return poisonRecipe; }
    public void setPoisonRecipe(String poisonRecipe) { this.poisonRecipe = poisonRecipe; }
    public int getPreparationTime() { return preparationTime; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }
    public String getEffectiveness() { return effectiveness; }
    public void setEffectiveness(String effectiveness) { this.effectiveness = effectiveness; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    @Override
    public String toString() {
        return name + " (" + source + ")";
    }
}