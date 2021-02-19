package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.Alumno;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.AlumnoDTO;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.AlumnoDTOConverter;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.profesor.Profesor;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.responsable.Responsable;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.AlumnoService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.ProfesorService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.ResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
public class AlumnoController {

	@Autowired
	private AlumnoService as;
	@Autowired
	private ProfesorService ps;
	@Autowired
	private ResponsableService rs;
	
	@Autowired
	AlumnoDTOConverter alumnoDTOConverter;
	
	/*
	 * 
	 * GET alumnos: 
	 * 
	 * */
	
	@ApiOperation(value = "Obtener todos los alumnos paginados",
			 notes = "Con este metodo conseguimos mandar todos los alumnos de 10 en 10. Así la Web podrá recoger los datos mas facilmente.")
	@GetMapping("/alumnos")
	public ResponseEntity<?> allAlumnos(@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Alumno> pagina = as.findAll(pageable);
		
		// transformar elementos de la pagina a DTO
				Page<AlumnoDTO> paginaDTO = pagina.map(new Function<Alumno, AlumnoDTO>() {
					@Override
					public AlumnoDTO apply(Alumno a) {
						return alumnoDTOConverter.convertAlumnoToAlumnoDTO(a);
					}
				});

		return ResponseEntity.status(HttpStatus.OK).body(paginaDTO);
	}
	
	/*
	 * 
	 * GET alumno/{idAlumno}
	 * 
	 * */
	
	@ApiOperation(value = "Obtener un alumno por identificador",
			 notes = "Con este metodo conseguimos recoger la información de un Alumno específico.")
	@GetMapping("/alumno/{id}")
	public ResponseEntity<Alumno> getAlumno(@PathVariable String id) {

		Optional<Alumno> alumnoOptional = as.findById(id);
		if (alumnoOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(alumnoOptional.get());
		} else {
			throw new AlumnoNoEncontradoException(id);
		}
	}
	
	/*
	 * 
	 * POST alumno
	 * 
	 * */
	
	@ApiOperation(value = "Añadir un alumno",
			 notes = "Con este metodo conseguimos añadir un Alumno.")
	@PostMapping("/alumno")
	public String postAlumno(@RequestBody Alumno nuevoAlumno) {
		return as.save(nuevoAlumno).toString();
	}
	
	/*
	 * 
	 * PUT alumno
	 * 
	 * */
	
	@ApiOperation(value = "Modificar un alumno",
			 notes = "Con este metodo conseguimos modificar un Alumno.")
	@PutMapping("/alumno")
	public String putAlumno(@RequestBody Alumno editadoAlumno) {
		return as.edit(editadoAlumno).toString();
	}
	
	
	
	/*
	 * 
	 * DELETE alumno/{idAlumno}
	 * 
	 **/
	
	@ApiOperation(value = "Borrar un alumno",
			 notes = "Con este metodo conseguimos borrar un Alumno por identificador. De esta forma conseguiremos borrar un Alumno específico.")
	@DeleteMapping("/alumno/{id}")
	public String deleteAlumno(@PathVariable String id) {
		as.deleteById(id);
		return "OK";
	}
	
	/*
	 * 
	 * PUT alumno/{idAlumno}/profesor/{idProfesor}
	 * 
	 * */
	
	@ApiOperation(value = "Asociar un profesor a un Alumno",
			 notes = "Con este metodo conseguimos asociar un Profesor a un Alumno.")
	@PutMapping("/alumno/{idAlumno}/profesor/{idProfesor}")
	public ResponseEntity<String> asociarProfesorAlumno(@PathVariable String idAlumno, @PathVariable String idProfesor) {
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		Optional<Profesor> profesorOptional = ps.findById(idProfesor);
		Alumno alumnoEditado = null;
		
		if (alumnoOptional.isPresent() && profesorOptional.isPresent()) {
			alumnoEditado = alumnoOptional.get();
			alumnoEditado.setTutor(profesorOptional.get());
			return ResponseEntity.status(HttpStatus.OK).body(as.edit(alumnoEditado).toString());
		} else {
			throw new AlumnoNoEncontradoException(idAlumno, idProfesor);
		}
	}
	
	/*
	 * 
	 * POST alumno/{idAlumno}/responsable
	 * 
	 * */
	
	@ApiOperation(value = "Añadir un Responsable a un Alumno",
			 notes = "Con este metodo conseguimos añadir un Responsable a un Alumno.")
	@PostMapping("/alumno/{idAlumno}/responsable")
	public ResponseEntity<String> aniadirResponsableAlumno(@PathVariable String idAlumno, @RequestBody Responsable nuevoResponsable) {
		
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		Alumno alumnoModificado = null;
		if(alumnoOptional.isPresent()) {
			alumnoModificado = alumnoOptional.get();
			List<Responsable> lista = alumnoModificado.getResponsables();
			lista.add(nuevoResponsable);
			alumnoModificado.setResponsables(lista);
			return ResponseEntity.status(HttpStatus.OK).body(as.save(alumnoModificado).toString());
		} else {
			throw new AlumnoNoEncontradoException(idAlumno);
		}
	}
	
	/*
	 * 
	 * DELETE alumno/{idAlumno}/responsable/{idResponsable}
	 * 
	 * */
	
	@ApiOperation(value = "Eliminar un Responsable a un Alumno",
			 notes = "Con este metodo conseguimos eliminar un Responsable a un Alumno. Pero no lo borramos de la BD solo lo desvinculamos de ese Alumno")
	@PostMapping("/alumno/{idAlumno}/responsable/{idResponsable}")
	public ResponseEntity<String> borrarResponsableAlumno(@PathVariable String idAlumno, @PathVariable Integer idResponsable) {
		
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		Alumno alumnoModificado = null;
		if(alumnoOptional.isPresent()) {
			alumnoModificado = alumnoOptional.get();
			List<Responsable> lista = alumnoModificado.getResponsables();
			lista.remove(idResponsable);
			alumnoModificado.setResponsables(lista);
			return ResponseEntity.status(HttpStatus.OK).body(as.save(alumnoModificado).toString());
		} else {
			throw new AlumnoNoEncontradoException(idAlumno);
		}
	}
}
