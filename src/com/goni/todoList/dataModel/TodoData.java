package com.goni.todoList.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoData {
    private static TodoData instance = new TodoData();
    private static ObservableList<TodoItem> todoItems;
    private static String fileName = "TodoItems.txt";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static TodoData getInstance() {
        return instance;
    }
    public ObservableList<TodoItem> getTodoItems(){
        return todoItems;
    }
    public void addTodoItem(TodoItem item){
        todoItems.add(item);
    }

    public static void setTodoItems(ObservableList<TodoItem> todoItems) {
        TodoData.todoItems = todoItems;
    }

    public void deleteTodoItem(TodoItem item){
        todoItems.remove(item);
    }

    public static void loadTodoItems(){
        todoItems = FXCollections.observableArrayList();
        try {
            Reader r = new FileReader("output.txt");
            BufferedReader br = new BufferedReader(r);
            String line;
            while((line=br.readLine())!=null){
            String[] pieces = line.split("\t");
            String sd = pieces[0];
            String ld = pieces[1];
            LocalDate dl = LocalDate.parse(pieces[2],formatter);
//          System.out.println(dl.toString());
//          System.out.println(pieces[2]);
            todoItems.add(new TodoItem(ld,sd,dl));
        }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }


    public static void storeTodoItems(){
        try {
            Writer w = new FileWriter("output.txt");
            Iterator<TodoItem> i = todoItems.iterator();
            while(i.hasNext()) {
                TodoItem item = i.next();
w.write(item.shortDescription+"\t"+item.longDescription+"\t"+item.getDeadLine()+"\n");
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
