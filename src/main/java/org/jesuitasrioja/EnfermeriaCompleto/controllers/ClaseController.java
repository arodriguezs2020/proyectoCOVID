package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import java.util.Optional;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.clase.Clase;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class ClaseController {
	@Autowired
	private ClaseService cs;
	
	/*
	 * 
	 * GET clase/{idClase}
	 * 
	 * */
	
	@ApiOperation(value = "Obtener una clase por identificador",
			 notes = "Con este metodo conseguimos recoger la información de una clase específica.")
	@GetMapping("/clase/{id}")
	public ResponseEntity<Clase> getClase(@PathVariable String id) {

		Optional<Clase> claseOptional = cs.findById(id);
		if (claseOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(claseOptional.get());
		} else {
			throw new ClaseNoEncontradaException(id);
		}
	}
	
	/*
	 * 
	 * POST clase
	 * 
	 * */
	
	@ApiOperation(value = "Añadir una clase",
			 notes = "Con este metodo conseguimos añadir una Clase.")
	@PostMapping("/clase")
	public String postClase(@RequestBody Clase nuevaClase) {
		return cs.save(nuevaClase).toString();
	}
	
	/*
	 * 
	 * DELETE clase/{idClase}
	 * 
	 **/
	
	@ApiOperation(value = "Borrar una clase",
			 notes = "Con este metodo conseguimos borrar una Clase por identificador. De esta forma conseguiremos borrar una Clase específico.")
	@DeleteMapping("/clase/{id}")
	public String deleteClase(@PathVariable String id) {
		cs.deleteById(id);
		return "OK";
	}
	
}
