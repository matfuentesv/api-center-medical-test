package cl.company.center.medical.repository;

import cl.company.center.medical.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM Usuario u WHERE u.usuario = :usuario  AND u.contrasena = :contrasena")
    Optional<User> findByUserPassword(@Param("usuario") String usuario,@Param("contrasena") String contrasena);

    @Query("SELECT u FROM Usuario u WHERE u.usuario = :usuario")
    Optional<User> findUser(@Param("usuario") String usuario);
}
