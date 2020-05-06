package org.andresoviedo.stabagotchi;

import java.io.Serializable;

public class Game implements Serializable{

    private Pet pet;

    public Game(Pet pet) {
        this.pet = pet;
    }

    public Pet getPet() {
        return pet;
    }

    public void levelUp() {
        int currentCoinPoints = pet.getCoinPoints();
        if (currentCoinPoints == 100) {
            pet.levelUp();
        }
    }

    public void replenishHealth(Foods food) {
        int foodHealthValue = food.getHpRestoreValue();
        int foodCost = food.getCostOfFood();
        pet.increaseHealthBy(foodHealthValue, foodCost);
    }

    public void feed(Foods food) {
        if (food.getCostOfFood() <= pet.getCoinPoints()) {
            replenishHealth(food);
        }
    }

}
