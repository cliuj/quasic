package quasic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;


public class Launcher extends Application {

    private static Scene scene;

    public void start(Stage stage) {
        stage.setTitle("Quasic");

        scene = new Scene(new Window());

        stage.setScene(scene);
        stage.show();
    }

    class Window extends Pane {

        private TextField outputLength_inputField;
        private TextArea msg_inputField;

        private Label outputLength_label;
        private Label output_label;
        private Label input_label;
        private Label result_label;

        private String input;
        private String output;
        private int length;

        private Window() {
            init();
        }

        private void init() {
            setPrefSize(600, 200);
            Quasic quasic = Quasic.getInstance();
            output = "";
            initLabels();
            initInputFields(quasic);

        }

        private void initLabels() {
            input_label = new Label("Message input:");
            input_label.setLayoutX(10);
            input_label.setLayoutY(10);

            outputLength_label = new Label("Output size:");
            outputLength_label.setLayoutX(475);
            outputLength_label.setLayoutY(10);

            output_label = new Label("Output: ");
            output_label.setLayoutX(10);
            output_label.setLayoutY(170);

            result_label = new Label();
            result_label.setLayoutX(100);
            result_label.setLayoutY(180);

            getChildren().addAll(input_label, outputLength_label, output_label, result_label);
        }

        private void initInputFields(Quasic quasic) {
            outputLength_inputField = new TextField();
            outputLength_inputField.setLayoutX(550);
            outputLength_inputField.setLayoutY(10);
            outputLength_inputField.setPrefSize(40, 20);

            outputLength_inputField.textProperty().addListener((observable_len, oldValue_len, newValue_len) -> {
                if(newValue_len.length() < 3) {
                    try{
                        length = Integer.parseInt(newValue_len);

                        if(!output.isEmpty()) {
                            result_label.setText(new String(quasic.execute(output, length)));
                        }
                    } catch (Exception e){
                        System.out.println("Invalid input for output length. Please enter a number. . .");
                        result_label.setText("");
                    }
                } else {
                    outputLength_inputField.setText(outputLength_inputField.getText().substring(0, 2));
                }
            });

            msg_inputField = new TextArea();
            msg_inputField.setLayoutX(102);
            msg_inputField.setLayoutY(10);
            msg_inputField.setPrefSize(350, 150);

            msg_inputField.textProperty().addListener((observable_msg, oldValue_msg, newValue_msg) -> {
                try {
                    output = newValue_msg;
                    if(newValue_msg.isEmpty()) {

                        result_label.setText("");
                    }

                    if(length > 0) {
                        result_label.setText(new String(quasic.execute(output, length)));
                    }
                } catch (Exception e) {

                }
            });

            getChildren().addAll(outputLength_inputField, msg_inputField);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
