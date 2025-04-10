module com.slantiz.epicgame {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
	requires javafx.graphics;
	requires org.yaml.snakeyaml;
	requires org.slf4j;

    opens com.slantiz.epicgame to javafx.fxml;
    exports com.slantiz.epicgame;
}