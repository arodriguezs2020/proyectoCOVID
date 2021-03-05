package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IncidenciaNoEncontradaException extends RuntimeException{

	public IncidenciaNoEncontradaException(Long idIncidencia) {
		super("incidencia with " + idIncidencia + " can not be retrieved");
	}
}
