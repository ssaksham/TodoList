package com.goni.todoList;
import com.goni.todoList.dataModel.TodoData;
import com.goni.todoList.dataModel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

    public class dialogController {
    @FXML
    TextField shortDescription;
    @FXML
    TextArea detailedDescription;
    @FXML
    DatePicker deadLine;
    public TodoItem processResult()
    {
    TodoItem item = new TodoItem(detailedDescription.getText(),shortDescription.getText(), deadLine.getValue());
    TodoData.getInstance().addTodoItem(item);
    return item;
    }
}
