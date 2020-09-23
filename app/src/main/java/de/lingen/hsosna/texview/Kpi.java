package de.lingen.hsosna.texview;

import java.util.Date;

public class Kpi {
    private String name;
    private int currentValue;
    private int maxValue;
    private String timestamp;

    public Kpi (String name, int currentValue, int maxValue, String timestamp) {
        this.name = name;
        this.currentValue = currentValue;
        this.maxValue = maxValue;
        this.timestamp = timestamp;


    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public int getCurrentValue () {
        return currentValue;
    }

    public void setCurrentValue (int currentValue) {
        this.currentValue = currentValue;
    }

    public int getMaxValue () {
        return maxValue;
    }

    public void setMaxValue (int maxValue) {
        this.maxValue = maxValue;
    }

    public String getTimestamp () {
        return timestamp;
    }

    public void setTimestamp (String timestamp) {
        this.timestamp = timestamp;
    }
}
