package edu.umg.car;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static java.lang.System.exit;

@SpringBootApplication
public class CarUIApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(CarUIApp.class);

    private ConfigurableApplicationContext context;

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void init() throws Exception {
        logger.info("Loading spring context");
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CarUIApp.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            logger.info("Loading Java FX context");
            ClassLoader classLoader = getClass().getClassLoader();
            FXMLLoader loader = new FXMLLoader(classLoader.getResource("fxml/Main.fxml"));
            loader.setControllerFactory(context::getBean);
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setTitle("Bluetooth Car Control UI");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            logger.error("Can't load FX form: ", e);
            exit(2);
        }
    }

    @Override
    public void stop() throws Exception {
        logger.info("closing application");
        context.close();
    }

    public static void main(String [] args) {
        launch(args);
    }
}
