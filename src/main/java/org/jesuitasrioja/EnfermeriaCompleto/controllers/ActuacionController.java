package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import java.util.List;
import java.util.Optional;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.PCR.PCR;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.actuacion.Actuacion;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.Alumno;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.Incidencia;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.ActuacionService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.IncidenciaService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.PcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class ActuacionController {
	
	@Autowired
	private ActuacionService as;
	
	@Autowired
	private IncidenciaService is;
	
	@Autowired
	private PcrService ps;
	
	/*
	 * 
	 * POST actuacion/incidencia/{idIncidencia}
	 * 
	 * */
	
	@ApiOperation(value = "Crear una nueva actuacion dentro de una incidencia",
			 notes = "Con este metodo conseguimos crear una nueva actuación pasandole el identificador de la incidencia y toda la informacion de la nueva Actuacion.")
	@PostMapping("/actuacion/incidencia/{id}")
	public String postAlumno(@RequestBody Actuacion nuevaActuacion, @PathVariable Long id) {
		
		Optional<Incidencia> incidenciaOptional = is.findById(id);
		
		if (incidenciaOptional.isPresent()) {
			
			Incidencia incidencia = incidenciaOptional.get();
			nuevaActuacion.setIncidencia(incidencia);
			return as.save(nuevaActuacion).toString();
			
		} else {
			throw new IncidenciaNoEncontradaException(id);
		}
		
		
	}
	
	/*
	 * 
	 * DELETE actuacion/{idActuacion}
	 * 
	 **/
	
	@ApiOperation(value = "Borrar una actuacion",
			 notes = "Con este metodo conseguimos borrar una Actuación por identificador y todos los PCR que tenga asociados. De esta forma conseguiremos borrar una Actuación específica.")
	@DeleteMapping("/actuacion/{id}")
	public String deleteActuacion(@PathVariable String id) {
		
		as.deleteById(id);
		Optional<Actuacion> actuacion = as.findById(id);
		
		if (actuacion.isPresent()) {
			Actuacion act = actuacion.get();
			for ( PCR pcr : act.getListaPCR()) {
				ps.deleteById(pcr.getIdentificador());
			}
		}
		
		return "OK";
	}
	
	/*
	 * 
	 * GET actuacion/{idActuacion}
	 * 
	 * */
	
	@ApiOperation(value = "Obtener una actuación por identificador",
			 notes = "Con este metodo conseguimos recoger la información de una Actuación específica.")
	@GetMapping("/actuacion/{id}")
	public ResponseEntity<Actuacion> getActuacion(@PathVariable String id) {

		Optional<Actuacion> actuacionOptional = as.findById(id);
		if (actuacionOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(actuacionOptional.get());
		} else {
			throw new ActuacionNoEncontradaException(id);
		}
	}
	
	/*
	 * 
	 * POST actuacion/{idActuacion}/pcr
	 * 
	 * */
	
	@ApiOperation(value = "Añadir un pcr a una actuación",
			 notes = "Con este metodo conseguimos añadir un pcr a una actuación.")
	@PostMapping("/actuacion/{id}/pcr")
	public String postActuacion(@PathVariable String id, @RequestBody PCR pcr) {
		
		Optional<Actuacion> actuacionOptional = as.findById(id);
		if (actuacionOptional.isPresent()) {
			Actuacion actuacion = actuacionOptional.get();
			
			List<PCR> listaPCR = actuacion.getListaPCR();
			listaPCR.add(pcr);
			actuacion.setListaPCR(listaPCR);
			
			return as.save(actuacion).toString();
		} else {
			throw new ActuacionNoEncontradaException(id);
		}
		
	}
	
	/*
	 * 
	 * DELETE actuacion/pcr/{idPCR}
	 * 
	 **/
	
	@ApiOperation(value = "Borrar una actuacion",
			 notes = "Con este metodo conseguimos borrar una Actuación por identificador y todos los PCR que tenga asociados. De esta forma conseguiremos borrar una Actuación específica.")
	@DeleteMapping("/actuacion/pcr/{id}")
	public String deletePCR(@PathVariable Long id) {
		ps.deleteById(id);
		List<Actuacion> actuaciones = as.findAll();
		
		for (Actuacion actuacion : actuaciones) {
			List<PCR> lista = actuacion.getListaPCR();
			for (PCR pcr : lista) {
				if (pcr.getIdentificador() == id) {
					lista.remove(pcr);
				}
			}
		}
		
		return "OK";
	}
	
	/*
	 * 
	 * PUT actuacion/pcr/{idPCR}
	 * 
	 * */
	
	@ApiOperation(value = "Modificar un PCR",
			 notes = "Con este metodo conseguimos modificar un PCR.")
	@PutMapping("/actuacion/pcr/{idPcr}")
	public ResponseEntity<String> modificarPCR(@RequestBody PCR pcr, @PathVariable Long idPcr) {
		Optional<PCR> pcrOptional = ps.findById(idPcr);
		
		if (pcrOptional.isPresent()) {
			
			PCR pcrEditado = pcrOptional.get();
			
			pcrEditado.setEstado(pcr.getEstado());
			pcrEditado.setFecha(pcr.getFecha());
			pcrEditado.setTipo(pcr.getTipo());
			
			return ResponseEntity.status(HttpStatus.OK).body(ps.edit(pcrEditado).toString());
		} else {
			throw new PcrNoEncontradoException(idPcr);
		}
	}

}
