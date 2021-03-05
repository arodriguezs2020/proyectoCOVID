package org.jesuitasrioja.EnfermeriaCompleto.persistencia.repositories;


import java.util.Date;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.Incidencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long>{

	public Page<Incidencia> findByFechaBetween(Date fechaInicio, Date fechaFin, Pageable pagueable);
}
