package org.jesuitasrioja.EnfermeriaCompleto.persistencia.repositories;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.profesor.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, String>{

}
