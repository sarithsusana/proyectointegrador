package controller;

import data.PrestamoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modelo.HorarioSala;
import modelo.Prestamo;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class GestionCalendarioController {

    @FXML private TableView<HorarioSala> tablaCalendario;
    @FXML private TableColumn<HorarioSala, String> colHorario;
    @FXML private TableColumn<HorarioSala, String> colSala1;
    @FXML private TableColumn<HorarioSala, String> colSala2;
    @FXML private TableColumn<HorarioSala, String> colSala3;

    private PrestamoDAO prestamoDAO;
    private ObservableList<HorarioSala> listaHorarios;

    @FXML
    public void initialize() {
        prestamoDAO = new PrestamoDAO();

        colHorario.setCellValueFactory(cell -> cell.getValue().horarioProperty());
        colSala1.setCellValueFactory(cell -> cell.getValue().sala1Property());
        colSala2.setCellValueFactory(cell -> cell.getValue().sala2Property());
        colSala3.setCellValueFactory(cell -> cell.getValue().sala3Property());

        cargarDatos();
    }

    private void cargarDatos() {
        String[] horarios = {
            "07:00 - 08:00", "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00",
            "11:00 - 12:00", "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00",
            "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00",
            "19:00 - 20:00"
        };

        listaHorarios = FXCollections.observableArrayList();

        try {
            List<Prestamo> prestamos = prestamoDAO.obtenerTodos();

            for (String h : horarios) {
                String[] parts = h.split(" - ");
                LocalTime inicio = LocalTime.parse(parts[0] + ":00");
                LocalTime fin = LocalTime.parse(parts[1] + ":00");

                String estadoSala1 = estaOcupado(prestamos, 1, inicio, fin) ? "Ocupado" : "Libre";
                String estadoSala2 = estaOcupado(prestamos, 2, inicio, fin) ? "Ocupado" : "Libre";
                String estadoSala3 = estaOcupado(prestamos, 3, inicio, fin) ? "Ocupado" : "Libre";

                listaHorarios.add(new HorarioSala(h, estadoSala1, estadoSala2, estadoSala3));
            }

            tablaCalendario.setItems(listaHorarios);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean estaOcupado(List<Prestamo> prestamos, int idSala, LocalTime inicio, LocalTime fin) {
        for (Prestamo p : prestamos) {
            if (p.getIdSala() == idSala) {
               
                LocalTime inicioPrestamo = p.getFechaHoraInicio().toLocalTime();
                LocalTime finPrestamo = p.getFechaHoraFin().toLocalTime();

                if (!(fin.isBefore(inicioPrestamo) || inicio.isAfter(finPrestamo))) {
                    return true;
                }
            }
        }
        return false;
    }
}
