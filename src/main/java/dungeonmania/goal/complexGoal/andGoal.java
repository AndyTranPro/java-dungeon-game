package dungeonmania.goal.complexGoal;

import java.io.Serializable;

import dungeonmania.goal.Goal;
import dungeonmania.goal.basicGoal.exitGoal;

public class andGoal implements Goal, Serializable{
    Goal firstGoal;
    Goal secGoal;

    public andGoal(Goal firstGoal, Goal secGoal) {
        this.firstGoal = firstGoal;
        this.secGoal = secGoal;
    }

    @Override
    public String evalGoal() {
        if (firstGoal.evalGoal().equals("") && secGoal.evalGoal().equals("")){
            return "";
        }
        if (firstGoal instanceof exitGoal && secGoal.evalGoal() != "") {
            return "(" + ":exit and " + secGoal.evalGoal() + ")";
        }
        if (secGoal instanceof exitGoal && firstGoal.evalGoal() != "") {
            return "(" + firstGoal.evalGoal() + " and :exit" + ")";
        }
        return "(" + firstGoal.evalGoal() + " and " + secGoal.evalGoal() + ")";
    }
    
    
}
