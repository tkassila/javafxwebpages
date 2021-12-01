package com.metait.javafxwebpages.datarow;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class WebAddressDataRowCell extends Pane {
    public WebAddressDataRowCell(String p_value, boolean p_bEditAble)
    {
        label.setText(p_value);
        this.setFocusTraversable(true);
        this.getChildren().add(label);
        bEditAble = p_bEditAble;
    }

    private boolean bEditAble = false;
    private Label label = new Label();
    private TextField textField = new TextField();
    private String strValue = null;
}
