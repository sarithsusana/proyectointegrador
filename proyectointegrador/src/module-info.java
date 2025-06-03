module proyectointegrador {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens controller to javafx.fxml;
    opens application to javafx.graphics, javafx.fxml;
    opens modelo to javafx.base, javafx.fxml;     
    opens utils to javafx.fxml;  
    opens view to javafx.fxml; 
   
}

