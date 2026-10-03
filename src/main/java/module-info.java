module com.slantiz.epicgame {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
	requires javafx.graphics;
	requires org.yaml.snakeyaml;
	requires org.slf4j;
	// Pulled in so the logging backend is present in jlink images too
	requires ch.qos.logback.classic;
	requires java.naming;

    opens com.slantiz.epicgame to javafx.fxml;
    exports com.slantiz.epicgame;
}