module com.koteseni.ijaproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires eu.hansolo.tilesfx;

    opens com.koteseni.ijaproj to javafx.fxml;
    exports com.koteseni.ijaproj;
}