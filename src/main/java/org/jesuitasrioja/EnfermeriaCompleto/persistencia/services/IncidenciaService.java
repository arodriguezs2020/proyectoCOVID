package org.jesuitasrioja.EnfermeriaCompleto.persistencia.services;

import java.util.Date;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.Incidencia;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.repositories.IncidenciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IncidenciaService extends BaseService<Incidencia, Long, IncidenciaRepository>{

	public Page<Incidencia> encontrarPorRangoFechas(Date fechaInicio, Date fechaFin, Pageable pagueable){
		return this.repositorio.findByFechaBetween(fechaInicio, fechaFin, pagueable);
	}
}
