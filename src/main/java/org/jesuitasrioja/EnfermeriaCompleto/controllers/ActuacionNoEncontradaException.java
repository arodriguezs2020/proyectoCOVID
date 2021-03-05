package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ActuacionNoEncontradaException extends RuntimeException {
	public ActuacionNoEncontradaException(String idActuacion) {
		super("actuacion with " + idActuacion + " can not be retrieved");
	}
}
