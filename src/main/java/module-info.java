module com.bogdan.battleship {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens com.bogdan.battleship to javafx.fxml;
    exports com.bogdan.battleship;
    exports com.bogdan.battleship.controller;
    exports com.bogdan.battleship.constant;
    exports com.bogdan.battleship.model;
    opens com.bogdan.battleship.controller to javafx.fxml;
}