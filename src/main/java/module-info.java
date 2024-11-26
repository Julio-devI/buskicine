module com.example.projetoptwo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens com.example.projetoptwo to javafx.fxml;
    exports com.example.projetoptwo;
    exports com.example.projetoptwo.controllers;
    opens com.example.projetoptwo.controllers to javafx.fxml;
}