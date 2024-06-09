module net.anvian.minecontrolfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;


    opens net.andrecarbajal.minecontrolfx to javafx.fxml;
    exports net.andrecarbajal.minecontrolfx;
    exports net.andrecarbajal.minecontrolfx.controller;
    opens net.andrecarbajal.minecontrolfx.controller to javafx.fxml;
}