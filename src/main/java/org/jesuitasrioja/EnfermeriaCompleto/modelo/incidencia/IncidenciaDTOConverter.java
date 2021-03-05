package org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncidenciaDTOConverter {
	@Autowired
	private final ModelMapper modelMapper;

	public IncidenciaDTOConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public IncidenciaDTO convertIncidenciaToIncidenciaDTO(Incidencia incidencia) {

		IncidenciaDTO dto = modelMapper.map(incidencia, IncidenciaDTO.class);

		return dto;
	}
}
