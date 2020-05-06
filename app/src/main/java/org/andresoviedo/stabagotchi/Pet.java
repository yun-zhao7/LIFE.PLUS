package org.andresoviedo.stabagotchi;


import java.io.Serializable;


public class Pet  implements Serializable {

    private String name;
    private int level;
    private int healthPoints;
    private int coinPoints;

    public Pet(String name) {
        this.name = name;
        this.level = 1;
        this.healthPoints = 100;

    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getCoinPoints() { return coinPoints; }

    public void setLevel(int level) {
        this.level = level;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
        if (healthPoints > 100) this.healthPoints = 100;
    }

    public void setCoinPoints(int coinPoints) {
        this.coinPoints = coinPoints;
    }

    public void addCoinPoint() {
        coinPoints++;
    }

    public void levelUp(){
        level++;
        coinPoints -= 100;
    }

    public void decreaseHealthByOne() {
        healthPoints--;
    }

    public boolean canAffordThisFood(int costOfFood) {
        return costOfFood <= getCoinPoints();
    }

    public void increaseHealthBy(int foodHealthValue, int foodCost) {
        if (canAffordThisFood(foodCost)) {
                int newHealthPoints = foodHealthValue + getHealthPoints();
                setHealthPoints(newHealthPoints);
                int newCoinPoints = coinPoints - foodCost;
                setCoinPoints(newCoinPoints);
        }
    }

}
