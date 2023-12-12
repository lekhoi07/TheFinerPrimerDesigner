package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * This class processes the input DNA template sequence the user supplies. It also allows the user to input the DNA
 * sequence in the first place.
 */
public class InputProcessor {
    private BorderPane root;
    private Sequence inputSequence;
    private Stage stage;
    private TheFinerPrimerDesigner designer;

    /**
     * This constructor initializes the instance variables and sets up the GUI.
     * @param stage
     * @param designer
     */
    public InputProcessor(Stage stage, TheFinerPrimerDesigner designer) {
        this.stage = stage;
        this.designer = designer;
        this.createGUI();
    }

    public BorderPane getRoot() {
        return this.root;
    }

    public Sequence getInput() {
        return this.inputSequence;
    }

    /**
     * This helper method sets up the GUI which with the user interacts to input a template sequence.
     */
    private void createGUI() {
        // Set up the stage into which the user can input data.
        this.root = new BorderPane();
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setSpacing(100);
        buttonPane.setPrefHeight(30);
        Button OKbutton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        buttonPane.getChildren().addAll(OKbutton, cancelButton);
        this.root.setTop(textArea);
        this.root.setBottom(buttonPane);

        // Set the OK button so that when clicked, the input is processed.
        OKbutton.setOnAction((ActionEvent e) -> {
            if (this.isValidSequence(textArea.getText())) {
                this.inputSequence = this.processSequence(textArea.getText());
                if (this.inputSequence.getSequence().length() > 38) {
                    this.designer.setInputSequence(this.inputSequence);
                    this.stage.close();
                } else {
                    new ErrorMessage("Input sequence too short");
                }
            } else {
                new ErrorMessage("Not a valid DNA sequence");
            }
        });

        // Set the cancel button so that when clicked, the stage is closed and nothing else occurs.
        cancelButton.setOnAction((ActionEvent e) -> this.stage.close());
    }

    /**
     * This helper method checks whether a user's input is a valid DNA sequence that primers can be generated on.
     * @param sequence
     * @return
     */
    private boolean isValidSequence(String sequence) {
        // The sequence is invalid if the user supplies nothing.
        if (sequence.isEmpty()) {
            return false;
        }

        // The input is valid if the input only consists of DNA base pairs and/or spaces.
        char[] allowedCharacters = {'a', 't', 'c', 'g', 'A', 'T', 'C', 'G', ' ', '\n'};
        for (int i = 0; i < sequence.length(); i++) {
            if (!this.isInCharArray(sequence.toCharArray()[i], allowedCharacters)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This is a helper method that returns true if a character is in a character array, false otherwise.
     * @param character
     * @param array
     * @return
     */
    private boolean isInCharArray(char character, char[] array) {
        for (char c : array) {
            if (character == c) {
                return true;
            }
        }

        return false;
    }

    /**
     * This helper cleans up the input by removing tabs, spaces, and extra lines. It also appends a 5 to the beginning
     * and a 3 to the end to mark the 5' and 3' ends of the DNA.
     * @param sequence
     * @return
     */
    private Sequence processSequence(String sequence) {
        sequence = sequence.replaceAll(" ","");
        sequence = sequence.replaceAll("\t", "");
        sequence = sequence.replaceAll("\n", "");
        sequence = "5" + sequence.toLowerCase() + "3";
        return new Sequence(sequence);
    }
}
