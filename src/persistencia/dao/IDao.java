package persistencia.dao;

import java.util.List;

public interface IDao <T>{
    T crear(T entidad);
    T buscarPorId(Integer id);

    List<T> buscarTodos();

    void borrarPorId(Integer id);

    T actualizar(T entidad);
}
