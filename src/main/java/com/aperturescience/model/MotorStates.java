package com.aperturescience.model;

public class MotorStates {
    private Integer base;
    private Integer neck;
    private Integer elbow;
    private Integer head;

    public MotorStates(){}

    public MotorStates(Integer head, Integer neck, Integer elbow, Integer base) {
        this.base = base;
        this.neck = neck;
        this.elbow = elbow;
        this.head = head;
    }

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public Integer getNeck() {
        return neck;
    }

    public void setNeck(Integer neck) {
        this.neck = neck;
    }

    public Integer getElbow() {
        return elbow;
    }

    public void setElbow(Integer elbow) {
        this.elbow = elbow;
    }

    public Integer getHead() {
        return head;
    }

    public void setHead(Integer head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "MotorStates{" +
                "base=" + base +
                ", elbow=" + elbow +
                ", neck=" + neck +
                ", head=" + head +
                '}';
    }
}
