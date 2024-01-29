module net.anvian.minecontrolfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens net.anvian.minecontrolfx to javafx.fxml;
    exports net.anvian.minecontrolfx;
    exports net.anvian.minecontrolfx.controller;
    opens net.anvian.minecontrolfx.controller to javafx.fxml;
}