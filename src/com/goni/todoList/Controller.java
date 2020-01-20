package com.goni.todoList;

import com.goni.todoList.dataModel.TodoData;
import com.goni.todoList.dataModel.TodoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller{

    @FXML
    BorderPane mainWindowPane;
    @FXML
    TextArea textArea;
    @FXML
    ListView listView;
    @FXML
    Label label;
    @FXML
    private ContextMenu listContextMenu;

    private SortedList<TodoItem> sortedList;
    private FilteredList<TodoItem> filteredList;
    private Predicate<TodoItem> wantAll;
    private  Predicate<TodoItem> wantTodays;
    public void deleteItem(TodoItem item){
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
alert.setTitle("Delete Todo Item");
alert.setHeaderText("Delete Todo Item:"+item.getShortDescription());
alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out");
Optional<ButtonType> result = alert.showAndWait();
if (result.isPresent()&&(result.get()==ButtonType.OK))
    TodoData.getInstance().deleteTodoItem(item);
}

@FXML
public void keyPressed(KeyEvent event){
        TodoItem selectedItem = (TodoItem)listView.getSelectionModel().getSelectedItem();
        if(selectedItem!=null)
       if (event.getCode().equals(KeyCode.DELETE))
        deleteItem(selectedItem);

}


    public void initialize(){

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        listContextMenu.getItems().add(deleteMenuItem);

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item = (TodoItem)listView.getSelectionModel().getSelectedItem();
deleteItem(item);
            }
        });
        wantAll = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return true;
            }
        };
        wantTodays = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                if (todoItem.getDeadLine().equals(LocalDate.now()))
                return true;
                return false;
            }
        };
        filteredList = new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems());
filteredList.setPredicate(wantAll);
//    TodoItem t1 = new TodoItem("Long 1","Short1", LocalDate.now());
//    TodoItem t2 = new TodoItem("Long 2","Short2", LocalDate.now());
//    ObservableList<TodoItem> todoItems =FXCollections.observableArrayList();
//  todoItems.add(t1);
//   todoItems.add(t2);
//        TodoData.setTodoItems(todoItems);
   //    TodoData.storeTodoItems();
//    TodoData.loadTodoItems();
        sortedList = new SortedList<TodoItem>(filteredList, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                if (o1.getDeadLine().isBefore(o2.getDeadLine()))
                    return 1;
                else
                    return -1;
            }
        });
        listView.setItems(sortedList);
        //listView.setItems(TodoData.getInstance().getTodoItems());
      //   listView.getItems().addAll(TodoData.getInstance().getTodoItems());
    listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);



    listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
    @Override
    public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
        if (newValue!=null) {
//            System.out.println(newValue.getShortDescription());
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            TodoItem t = newValue;
            textArea.setText(t.getLongDescription());
            label.setText((df.format(t.getDeadLine())));
        }
    }
});
        listView.getSelectionModel().selectFirst();

        listView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
            ListCell<TodoItem> cell = new ListCell<TodoItem>(){
                @Override
                protected void updateItem(TodoItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty)
                    setText(null);
                    else{
                        setText(item.getShortDescription());

                        if (item.getDeadLine().isBefore(LocalDate.now().plusDays(1)))
                            setTextFill(Color.RED);
                    else if (item.getDeadLine().equals(LocalDate.now().plusDays(1)))
                        setTextFill(Color.BROWN);
                    }
                }
                };
            cell.emptyProperty().addListener(
                    (obs, wasEmpty, isNowEmpty) -> {
                        if (isNowEmpty)
                            cell.setContextMenu(null);
                        else
                            cell.setContextMenu(listContextMenu);
                    }


            );
                return cell;
            }
            });
    }

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindowPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogPane.fxml"));
        try
        {
        dialog.getDialogPane().setContent(fxmlLoader.load());
        }
        catch (IOException e){
            System.out.println(e.getStackTrace());
            return;
        }
        dialog.setTitle("Add new Todo item");
        dialog.setHeaderText("Use this dialog to create new item");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()&&result.get()==ButtonType.OK){
            dialogController dialogController = fxmlLoader.getController();
            TodoItem item = dialogController.processResult();
//            listView.getItems().setAll(TodoData.getInstance().getTodoItems());
            listView.getSelectionModel().select(item);
        }
    }

public void handleFilter(ActionEvent event){
        TodoItem selectedItem = (TodoItem)listView.getSelectionModel().getSelectedItem();
        ToggleButton tglButton = (ToggleButton)event.getSource();
        if (tglButton.isSelected()){
            filteredList.setPredicate(wantTodays);
            if (filteredList.isEmpty()){
                textArea.clear();
            label.setText(null);
            }
            else if(filteredList.contains(selectedItem)){
                listView.getSelectionModel().select(selectedItem);
            }
            else {
                listView.getSelectionModel().selectFirst();
            }
        }
        else{
            filteredList.setPredicate(wantAll);
            listView.getSelectionModel().select(selectedItem);
        }
}

//public void onSelection(){
//TodoItem t= (TodoItem) listView.getSelectionModel().getSelectedItem();
//}
public void handleExit(){
    Platform.exit();
}

}