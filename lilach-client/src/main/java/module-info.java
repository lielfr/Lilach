module org.cshaifasweng.winter {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.springframework.boot;

    opens org.cshaifasweng.winter to javafx.fxml;
    exports org.cshaifasweng.winter;
}