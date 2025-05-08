// Module definition for the game
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

module com.koteseni.ijaproj {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.web;

    requires gson;
    requires java.sql;

    opens com.koteseni.ijaproj to javafx.fxml;
    opens com.koteseni.ijaproj.controller to javafx.fxml;
    opens com.koteseni.ijaproj.model to javafx.fxml, gson;
    opens com.koteseni.ijaproj.view to javafx.fxml;

    exports com.koteseni.ijaproj;
    exports com.koteseni.ijaproj.controller;
    exports com.koteseni.ijaproj.model;
    exports com.koteseni.ijaproj.view;
}
