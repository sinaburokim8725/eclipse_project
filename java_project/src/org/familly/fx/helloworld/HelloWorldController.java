package org.familly.fx.helloworld;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class HelloWorldController {

    public Label helloWorldLabel;

    public void sayHelloWorld(ActionEvent actionEvent) {
        helloWorldLabel.setText("Hello World");

    }
}
