package com.goni.todoList.dataModel;

import java.time.LocalDate;

public class TodoItem {
    String longDescription;
    String shortDescription;
    LocalDate deadLine;

    public TodoItem(String longDescription, String shortDescription, LocalDate deadLine) {
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
        this.deadLine = deadLine;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    @Override
    public String toString() {
        return shortDescription;
    }
}
