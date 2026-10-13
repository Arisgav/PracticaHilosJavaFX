package ni.edu.uam.practicahilos;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProcesadorController {

    // ---------- Controles: CLIENTES ----------
    @FXML private Label lblEstadoClientes;
    @FXML private Label lblPorcentajeClientes;
    @FXML private ProgressBar barraClientes;
    @FXML private Button btnIniciarClientes;
    @FXML private Button btnDetenerClientes;

    // ---------- Controles: PRODUCTOS ----------
    @FXML private Label lblEstadoProductos;
    @FXML private Label lblPorcentajeProductos;
    @FXML private ProgressBar barraProductos;
    @FXML private Button btnIniciarProductos;
    @FXML private Button btnDetenerProductos;


    @FXML private Button btnReiniciar;

    private Thread hiloClientes;
    private Thread hiloProductos;


    @FXML
    private void iniciarClientes() {

        // Evita iniciar el mismo proceso dos veces
        if (hiloClientes != null && hiloClientes.isAlive()) {
            return;
        }

        barraClientes.setProgress(0);
        lblPorcentajeClientes.setText("0%");
        lblEstadoClientes.setText("Procesando clientes...");
        btnIniciarClientes.setDisable(true);
        btnDetenerClientes.setDisable(false);

        Runnable tarea = crearTarea(
                100, 100,
                lblEstadoClientes, lblPorcentajeClientes, barraClientes,
                btnIniciarClientes, btnDetenerClientes
        );

        hiloClientes = new Thread(tarea, "Hilo-Clientes");
        hiloClientes.setDaemon(true);
        hiloClientes.start();
    }

    @FXML
    private void detenerClientes() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            btnDetenerClientes.setDisable(true);
            lblEstadoClientes.setText("Solicitando detención...");
            hiloClientes.interrupt();
        }
    }


    @FXML
    private void iniciarProductos() {

        if (hiloProductos != null && hiloProductos.isAlive()) {
            return;
        }

        barraProductos.setProgress(0);
        lblPorcentajeProductos.setText("0%");
        lblEstadoProductos.setText("Procesando productos...");
        btnIniciarProductos.setDisable(true);
        btnDetenerProductos.setDisable(false);

        Runnable tarea = crearTarea(
                50, 250,
                lblEstadoProductos, lblPorcentajeProductos, barraProductos,
                btnIniciarProductos, btnDetenerProductos
        );

        hiloProductos = new Thread(tarea, "Hilo-Productos");
        hiloProductos.setDaemon(true);
        hiloProductos.start();
    }

    @FXML
    private void detenerProductos() {
        if (hiloProductos != null && hiloProductos.isAlive()) {
            btnDetenerProductos.setDisable(true);
            lblEstadoProductos.setText("Solicitando detención...");
            hiloProductos.interrupt();
        }
    }

    private Runnable crearTarea(int totalRegistros,
                                long milisPorRegistro,
                                Label lblEstado,
                                Label lblPorcentaje,
                                ProgressBar barra,
                                Button btnIniciar,
                                Button btnDetener) {

        return () -> {
            try {
                for (int i = 1; i <= totalRegistros; i++) {

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(milisPorRegistro);

                    final double progreso = (double) i / totalRegistros;
                    final int porcentaje = (int) (progreso * 100);

                    // La interfaz solo se actualiza desde el hilo de JavaFX
                    Platform.runLater(() -> {
                        barra.setProgress(progreso);
                        lblPorcentaje.setText(porcentaje + "%");
                    });
                }

                Platform.runLater(() -> {
                    lblEstado.setText("Proceso finalizado");
                    btnIniciar.setDisable(false);
                    btnDetener.setDisable(true);
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                Platform.runLater(() -> {
                    lblEstado.setText("Proceso detenido");
                    btnIniciar.setDisable(false);
                    btnDetener.setDisable(true);
                });
            }
        };
    }

    @FXML
    private void reiniciarProceso() {

        if (hiloClientes == null || !hiloClientes.isAlive()) {
            barraClientes.setProgress(0);
            lblPorcentajeClientes.setText("0%");
            lblEstadoClientes.setText("Esperando inicio");
            btnIniciarClientes.setDisable(false);
            btnDetenerClientes.setDisable(true);
        }

        if (hiloProductos == null || !hiloProductos.isAlive()) {
            barraProductos.setProgress(0);
            lblPorcentajeProductos.setText("0%");
            lblEstadoProductos.setText("Esperando inicio");
            btnIniciarProductos.setDisable(false);
            btnDetenerProductos.setDisable(true);
        }
    }

    public void detenerAlCerrar() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            hiloClientes.interrupt();
        }
        if (hiloProductos != null && hiloProductos.isAlive()) {
            hiloProductos.interrupt();
        }
    }
}