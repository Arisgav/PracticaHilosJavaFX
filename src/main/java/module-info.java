module ni.edu.uam.practicahilosjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.practicahilos to javafx.fxml;
    exports ni.edu.uam.practicahilos;
}