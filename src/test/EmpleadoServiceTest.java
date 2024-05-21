package test;

import model.Empleado;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import persistencia.impl.EmpleadoDaoH2;
import service.EmpleadoService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmpleadoServiceTest {
    private static EmpleadoService empleadoService=new EmpleadoService(new EmpleadoDaoH2());

    private static Logger LOGGER = Logger.getLogger(EmpleadoServiceTest.class);
    @BeforeAll
    static void crearTablas(){
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/parcial1;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Testear empleado guardado")
    void testearEmpleadoGuardado() {
        Empleado empleado = new Empleado("Aldo","Fefo","43434", LocalDate.of(2024,04,22));

        Empleado empleadoDesdeLaBD = empleadoService.registrarEmpleado(empleado);
        assertNotNull(empleadoDesdeLaBD);

    }

    @Test
    @DisplayName("Testear busqueda empleado por ID")
    void testsEmpleadoPorID() {
        Integer id = 1;
        Empleado empleadoEncontrado=empleadoService.buscarPorId(id);
        assertEquals(id,empleadoEncontrado.getId());
    }


    @Test
    @DisplayName("Testear busqueda todos los empleados")
    void testBusquedaTodos() {
        Empleado empleado = new Empleado("Cosme","Manganito","443",LocalDate.of(2024,12,13));
        empleadoService.registrarEmpleado(empleado);

        List<Empleado> empleados = empleadoService.buscarTodos();
        assertEquals(1,empleados.size());

    }

    @Test
    @DisplayName("Testear borrar por ID")
    void testBorrarPorID() {
        Empleado empleado1 = new Empleado("Cosme","Manganito","443",LocalDate.of(2024,12,13));
        Empleado empleado2 = new Empleado("Politan","Manganita","44443",LocalDate.of(2020,12,13));

        empleadoService.registrarEmpleado(empleado1);
        empleadoService.registrarEmpleado(empleado2);
        empleadoService.borrarPorId(2);

        List<Empleado> empleados = empleadoService.buscarTodos();

        assertEquals(1,empleados.size());

    }


    @Test
    @DisplayName("Testear actualizar empleado")
    void testActualizar() {
        Empleado empleado1 = new Empleado("Cosme","Manganito","443",LocalDate.of(2024,12,13));
        Empleado empleado2 = new Empleado("Politan","Manganita","44443",LocalDate.of(2020,12,13));

        Empleado empleado3 = new Empleado(1,"Paco","Manganita","44443",LocalDate.of(2020,12,13));

        empleadoService.registrarEmpleado(empleado1);
        empleadoService.registrarEmpleado(empleado2);
        empleadoService.actualizar(empleado3);

        assertEquals("Paco",empleadoService.buscarPorId(1).getApellido());

    }


}