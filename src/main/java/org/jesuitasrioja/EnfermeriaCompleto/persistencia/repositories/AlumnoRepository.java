package org.jesuitasrioja.EnfermeriaCompleto.persistencia.repositories;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, String>{

}
