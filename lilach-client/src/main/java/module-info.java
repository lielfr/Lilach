module org.cshaifasweng.winter {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;

    opens org.cshaifasweng.winter to javafx.fxml;
    exports org.cshaifasweng.winter;
}