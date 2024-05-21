package persistencia.impl;

import db.H2Connection;
import model.Empleado;
import org.apache.log4j.Logger;
import persistencia.dao.IDao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDaoH2 implements IDao<Empleado> {
    private static String SQL_INSERT = "INSERT INTO EMPLEADOS VALUES(DEFAULT,?,?,?,?)";
    private static String SQL_SELECT_ID="SELECT * FROM EMPLEADOS WHERE ID=?";
    private static String SQL_SELECT_ALL="SELECT * FROM EMPLEADOS";

    private static String SQL_DELETE="DELETE FROM EMPLEADOS WHERE ID=?";

    private static String SQL_UPDATE="UPDATE EMPLEADOS SET APELLIDO=?,NOMBRE=?,DNI=?,FECHA_NACIMIENTO=? WHERE ID=?";
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
                empleadoAretornar = new Empleado(id,entidad.getApellido(),entidad.getNombre(),entidad.getDni(),entidad.getFechaNacimiento());
            }
            LOGGER.info("Empleado persistido "+empleadoAretornar);
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
                    LOGGER.info(ex.getMessage());
                    ex.printStackTrace();
                }
            }
            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
        finally
        {

            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        return empleadoAretornar;
    }

    @Override
    public Empleado buscarPorId(Integer id) {

        Connection connection = null;
        Empleado empleadoEncontrado=null;
        try
        {
            connection= H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                Integer IdDevuelto = resultSet.getInt(1);
                String apellido=resultSet.getString(2);
                String nombre=resultSet.getString(3);
                String dni=resultSet.getString(4);
                LocalDate fecha=resultSet.getDate(5).toLocalDate();


                empleadoEncontrado = new Empleado(IdDevuelto,apellido,nombre,dni,fecha);

                LOGGER.info("empleado encontrado "+empleadoEncontrado);
            }

        }catch(Exception e)
        {

            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return empleadoEncontrado;
    }

    @Override
    public List<Empleado> buscarTodos() {


        List<Empleado> empleados=new ArrayList<>();

        Connection connection = null;
        try
        {
            connection = H2Connection.getConnection();
            Statement statment = connection.createStatement();
            ResultSet resultSet = statment.executeQuery(SQL_SELECT_ALL);
            while(resultSet.next())
            {
                Integer IdDevuelto = resultSet.getInt(1);
                String apellido=resultSet.getString(2);
                String nombre=resultSet.getString(3);
                String dni=resultSet.getString(4);
                LocalDate fecha=resultSet.getDate(5).toLocalDate();


                Empleado empleado = new Empleado(IdDevuelto,apellido,nombre,dni,fecha);
                LOGGER.info("Empleado listado "+empleado);
                empleados.add(empleado);

            }

        }catch(Exception e)
        {

            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        return empleados;
    }

    @Override
    public void borrarPorId(Integer id) {
        Connection connection = null;
        Empleado empleado = buscarPorId(id);
        try {

            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            Statement statment = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
            preparedStatement.setInt(1,id);

            preparedStatement.executeUpdate();

            LOGGER.info("Empleado borrado "+empleado);
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
                LOGGER.info(ex.getMessage());
                ex.printStackTrace();
            }
        }
        LOGGER.info(e.getMessage());
        e.printStackTrace();
    }
        finally
    {

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    }

    @Override
    public Empleado actualizar(Empleado entidad) {
        Connection connection = null;
        Empleado empleadoActualizado=null;
        try {

            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,entidad.getApellido());
            preparedStatement.setString(2,entidad.getNombre());
            preparedStatement.setString(3,entidad.getDni());
            preparedStatement.setDate(4,Date.valueOf(entidad.getFechaNacimiento()));
            preparedStatement.setInt(5,entidad.getId());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();


            while(resultSet.next())
            {
                LOGGER.info("Result Set "+ resultSet);

                Integer IdDevuelto = resultSet.getInt(1);

                empleadoActualizado= new Empleado(IdDevuelto,entidad.getApellido(), entidad.getNombre(), entidad.getDni(), entidad.getFechaNacimiento());

        }
            LOGGER.info("Empleado actualizado "+empleadoActualizado);
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
                    LOGGER.info(ex.getMessage());
                    ex.printStackTrace();
                }
            }
            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
        finally
        {

            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return empleadoActualizado;
    }
}
