package gm.tareas.presentacion;

import gm.tareas.TareasApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class SistemaTareasFx extends Application {

    //importamos la fabrica de spring
    private ConfigurableApplicationContext applicationContext;

   // public static void main(String[] args) {
     //   launch(args);
    //}
    //se ejecuta antes del metodo start
    @Override
    public void init(){
        this.applicationContext = new SpringApplicationBuilder(TareasApplication.class).run();
        //al momento de ejecutar nuestro metodo main,se vamnda a ejecutar la app fx,se ejectura el metodo init,y entonces se carga la fabrica de spring\

    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(TareasApplication.class.getResource("/templates/index.fxml")); //cargamos en memoria la interface
        loader.setControllerFactory(applicationContext::getBean); //proporcionamos todos los objetos del tipo Bean
        Scene escena = new Scene(loader.load());
        stage.setScene(escena);
        stage.show();
    }

    @Override
    public void stop(){
        applicationContext.close();
        Platform.exit();
    }
}
