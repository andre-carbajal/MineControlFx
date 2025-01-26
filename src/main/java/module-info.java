module net.anvian.minecontrolfx {
    uses net.andrecarbajal.minecontrolfx.loader.ILoader;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires com.google.gson;


    opens net.andrecarbajal.minecontrolfx to javafx.fxml;
    exports net.andrecarbajal.minecontrolfx;
    exports net.andrecarbajal.minecontrolfx.controller;
    opens net.andrecarbajal.minecontrolfx.controller to javafx.fxml;
}