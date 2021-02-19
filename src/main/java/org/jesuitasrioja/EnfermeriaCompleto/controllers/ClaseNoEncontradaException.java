package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ClaseNoEncontradaException extends RuntimeException{
	public ClaseNoEncontradaException(String idClase) {
		super("class with " + idClase + " can not be retrieved");
	}
}
