package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Arrays;
import java.util.Objects;

public class InputProcessor {
    private BorderPane root;
    private Sequence inputSequence;
    private Stage stage;
    private TheFinerPrimerDesigner designer;

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

    private void createGUI() {
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

        cancelButton.setOnAction((ActionEvent e) -> this.stage.close());
    }

    private boolean isValidSequence(String sequence) {
        if (sequence.isEmpty()) {
            return false;
        }

        char[] allowedCharacters = {'a', 't', 'c', 'g', 'A', 'T', 'C', 'G', ' ', '\n'};
        for (int i = 0; i < sequence.length(); i++) {
            if (!this.isInCharArray(sequence.toCharArray()[i], allowedCharacters)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInCharArray(char character, char[] array) {
        for (char c : array) {
            if (character == c) {
                return true;
            }
        }

        return false;
    }

    private Sequence processSequence(String sequence) {
        sequence = sequence.replaceAll(" ","");
        sequence = sequence.replaceAll("\t", "");
        sequence = sequence.replaceAll("\n", "");
        sequence = "5" + sequence.toLowerCase() + "3";
        return new Sequence(sequence);
    }
}
