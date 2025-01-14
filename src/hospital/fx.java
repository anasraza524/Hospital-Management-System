package hospital;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class fx extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create components
        TextField taskField = new TextField();
        Button addTaskButton = new Button("Add Task");
        Button removeTaskButton = new Button("Remove Selected Task");
        ListView<String> taskListView = new ListView<>();

        // Add task functionality
        addTaskButton.setOnAction(event -> {
            String task = taskField.getText();
            if (!task.isEmpty()) {
                taskListView.getItems().add(task);
                taskField.clear();
            }
        });

        // Remove task functionality
        removeTaskButton.setOnAction(event -> {
            String selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the task?");
                confirmation.showAndWait().ifPresent(response -> {
//                    if (response == ButtonType.OK) {
//                        taskListView.getItems().remove(selectedTask);
//                    }
                });
            }
        });

        // Layout setup
        VBox layout = new VBox(10, taskField, addTaskButton, taskListView, removeTaskButton);
        Scene scene = new Scene(layout, 300, 250);

        // Show window
        primaryStage.setTitle("To-Do List Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
