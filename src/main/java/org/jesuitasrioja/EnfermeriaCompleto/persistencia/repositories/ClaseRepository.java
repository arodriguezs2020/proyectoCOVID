package org.jesuitasrioja.EnfermeriaCompleto.persistencia.repositories;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.clase.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, String>{

}
