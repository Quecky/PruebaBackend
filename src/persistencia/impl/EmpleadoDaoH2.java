package persistencia.impl;

import db.H2Connection;
import model.Empleado;
import org.apache.log4j.Logger;
import persistencia.dao.IDao;

import java.sql.*;
import java.util.List;

public class EmpleadoDaoH2 implements IDao<Empleado> {
    private static String SQL_INSERT = "INSERT INTO EMPLEADOS VALUES(DEFAULT,?,?,?,?)";
    private static String SQL_SELECT_ID="SELECT * FROM DOMICILIOS WHERE ID=?";

    public static Logger LOGGER = Logger.getLogger(EmpleadoDaoH2.class);
    @Override
    public Empleado crear(Empleado entidad) {
        Empleado empleadoAretornar=null;
        Connection connection = null;
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,entidad.getApellido());
            preparedStatement.setString(2,entidad.getNombre());
            preparedStatement.setString(3,entidad.getDni());
            preparedStatement.setDate(4,Date.valueOf(entidad.getFechaNacimiento()));
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next())
            {
                Integer id = resultSet.getInt(1);
                domicilioAretornar = new Domicilio(id,domicilio.getCalle(),domicilio.getNumero(),domicilio.getLocalidad(),domicilio.getProvincia());
            }
            LOGGER.info("Paciente persistido "+domicilioAretornar);
            //el commit es para hacer modificaciones en la BD
            //no se necesita para listar
            connection.commit();

            connection.setAutoCommit(true);

        }catch(Exception e)
        {
            if(connection!=null)
            {
                try{
                    connection.rollback();
                }catch(SQLException ex)
                {
                    LOGGER.info(e.getMessage());
                    e.printStackTrace();
                }
            }
            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            connection.close();
        }


        return empleadoAretornar;
    }

    @Override
    public Empleado buscarPorId(Integer id) {
        return null;
    }

    @Override
    public List<Empleado> buscarTodos() {
        return null;
    }

    @Override
    public void borrarPorId(Integer id) {

    }

    @Override
    public Empleado actualizar(Empleado entidad) {
        return null;
    }
}
