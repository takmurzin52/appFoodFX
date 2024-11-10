module com.example.appfoodfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.appfoodfx to javafx.fxml;
    opens com.example.appfoodfx1.GUI to javafx.fxml;
    exports com.example.appfoodfx;
    exports com.example.appfoodfx1.models to javafx.fxml;
    exports com.example.appfoodfx1.GUI to javafx.fxml;
}