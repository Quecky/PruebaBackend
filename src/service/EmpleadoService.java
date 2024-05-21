package service;

import model.Empleado;
import persistencia.dao.IDao;

import java.sql.SQLException;
import java.util.List;

public class EmpleadoService {

    private IDao<Empleado> empleadoIDAO;

    public EmpleadoService(IDao<Empleado> empleadoIDAO) {
        this.empleadoIDAO = empleadoIDAO;
    }

    public Empleado registrarEmpleado(Empleado empleado) {
        return empleadoIDAO.crear(empleado);
    }

    public Empleado buscarPorId(Integer id) {
        return empleadoIDAO.buscarPorId(id);
    }

    public List<Empleado> buscarTodos() {
        return empleadoIDAO.buscarTodos();
    }


    public void borrarPorId(Integer id) {
        empleadoIDAO.borrarPorId(id);
    }

    public Empleado actualizar(Empleado entidad) {
    return empleadoIDAO.actualizar(entidad);
    }

}