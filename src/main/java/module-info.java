module com.metait.javafxwebpages {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.base;
    requires java.desktop;
    requires com.google.gson;
    requires com.ibm.icu;

    opens com.metait.javafxwebpages to javafx.fxml;
//    opens com.metait.javafxwebpages.datarow to javafx.fxml;
    exports com.metait.javafxwebpages.datarow;
    exports com.metait.javafxwebpages;
}