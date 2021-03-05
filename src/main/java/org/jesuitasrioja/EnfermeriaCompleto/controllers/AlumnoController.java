package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.Alumno;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.AlumnoDTO;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.AlumnoDTOConverter;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.clase.Clase;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.datosMedicos.DatosMedicos;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.Incidencia;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.profesor.Profesor;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.responsable.Responsable;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.AlumnoService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.ClaseService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.IncidenciaService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.ProfesorService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.ResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.springframework.web.bind.annotation.RequestParam;
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
	private IncidenciaService is;
	@Autowired
	private ClaseService cs;
	
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
		
		if (alumnoOptional.isPresent() && profesorOptional.isPresent()) {
			Alumno alumnoEditado = alumnoOptional.get();
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
		if(alumnoOptional.isPresent()) {
			Alumno alumnoModificado = alumnoOptional.get();
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
	@DeleteMapping("/alumno/{idAlumno}/responsable/{idResponsable}")
	public ResponseEntity<String> borrarResponsableAlumno(@PathVariable String idAlumno, @PathVariable Long idResponsable) {
		
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		if(alumnoOptional.isPresent()) {
			Alumno alumnoModificado = alumnoOptional.get();
			List<Responsable> lista = alumnoModificado.getResponsables();
			Optional<Responsable> responsableBorrar = rs.findById(idResponsable);
			lista.remove(responsableBorrar.get());
			alumnoModificado.setResponsables(lista);
			return ResponseEntity.status(HttpStatus.OK).body(as.save(alumnoModificado).toString());
		} else {
			throw new AlumnoNoEncontradoException(idAlumno);
		}
	}
	
	
	/*
	 * 
	 * POST alumno/{idAlumno}/incidencia
	 * 
	 * */
	
	@ApiOperation(value = "Añadir un incidencia al alumno",
			 notes = "Con este metodo conseguimos añadir una nueva incidencia a un alumno.")
	@PostMapping("/alumno/{id}/incidencia")
	public ResponseEntity<String> postIncidenciaAlumno(@RequestBody Incidencia nuevaIncidencia, @PathVariable String id) {
		
		Optional<Alumno> alumnoOptional = as.findById(id);
		if(alumnoOptional.isPresent()) {
			Alumno alumnoModificado = alumnoOptional.get();
			List<Incidencia> lista = alumnoModificado.getIncidencias();
			lista.add(nuevaIncidencia);
			alumnoModificado.setIncidencias(lista);
			return ResponseEntity.status(HttpStatus.OK).body(as.save(alumnoModificado).toString());
		} else {
			throw new AlumnoNoEncontradoException(id);
		}
	}
	
	/*
	 * 
	 * DELETE alumno/{idAlumno}/incidencia/{idIncidencia}
	 * 
	 * */
	
	@ApiOperation(value = "Eliminar una Incidencia de un Alumno",
			 notes = "Con este metodo conseguimos eliminar una incidencia de un Alumno. Pero no lo borramos de la BD solo lo desvinculamos de ese Alumno")
	@DeleteMapping("/alumno/{idAlumno}/incidencia/{idIncidencia}")
	public ResponseEntity<String> borrarIncidenciaAlumno(@PathVariable String idAlumno, @PathVariable Long idIncidencia) {
		
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		if(alumnoOptional.isPresent()) {
			Alumno alumnoModificado = alumnoOptional.get();
			List<Incidencia> lista = alumnoModificado.getIncidencias();
			Optional<Incidencia> incidenciaBorrar = is.findById(idIncidencia);
			lista.remove(incidenciaBorrar.get());
			alumnoModificado.setIncidencias(lista);
			return ResponseEntity.status(HttpStatus.OK).body(as.save(alumnoModificado).toString());
		} else {
			throw new AlumnoNoEncontradoException(idAlumno);
		}
	}
	
	/*
	 * 
	 * GET alumno/{idAlumno}/incidencias?from=<fechaInicio>&to=<fechafin>
	 * 
	 * */
	
	@ApiOperation(value = "Obtener todas las incidencias de un Alumno especifico",
			 notes = "Con este metodo conseguimos recoger todas las incidencias de un Alumno especifico, que esten entre dos fechas.")
	@GetMapping("/alumno/{id}/incidencias")
	public ResponseEntity<?> getIncidenciasAlumnoRango(@PageableDefault(size = 10, page = 0) Pageable pageable, @PathVariable String id, @RequestParam(name = "from") Date from, @RequestParam(name = "to") Date to) {
		Page<Incidencia> incidenciasAlumno = null;
		Optional<Alumno> alumnoOptional = as.findById(id);
		if(alumnoOptional.isPresent()) {			
			
			Page<Incidencia> incidencias = is.encontrarPorRangoFechas(from, to, pageable);
			
			List<Incidencia> incidenciasss = incidencias.getContent();
			
			for (Incidencia incidencia : incidenciasss) {
				if (incidencia.getAlumno().getIdentificador().equals(id)) {
					List<Incidencia> lista = new ArrayList<Incidencia>();
					lista.add(incidencia);
					incidenciasAlumno = new PageImpl<Incidencia>(lista, pageable, lista.size());
				}
			}			
		
			return ResponseEntity.status(HttpStatus.OK).body(incidenciasAlumno.get());
		}else {
			throw new AlumnoNoEncontradoException(id);
		}
	}
	
	/*
	 * 
	 * PUT alumno/{idAlumno}/clase/{idClase}
	 * 
	 * */
	
	@ApiOperation(value = "Asociar una Clase a un Alumno",
			 notes = "Con este metodo conseguimos asociar una Clase a un Alumno.")
	@PutMapping("/alumno/{idAlumno}/clase/{idClase}")
	public ResponseEntity<String> asociarClaseAlumno(@PathVariable String idAlumno, @PathVariable String idClase) {
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		Optional<Clase> claseOptional = cs.findById(idClase);
		
		if (alumnoOptional.isPresent() && claseOptional.isPresent()) {
			Alumno alumnoEditado = alumnoOptional.get();
			alumnoEditado.setClase(claseOptional.get());
			return ResponseEntity.status(HttpStatus.OK).body(as.edit(alumnoEditado).toString());
		} else {
			throw new AlumnoNoEncontradoException(idAlumno, idClase);
		}
	}
	
	/*
	 * 
	 * POST alumno/{idAlumno}/datosmedicos
	 * 
	 * */
	
	@ApiOperation(value = "Añadir los datos medicos de un Alumno",
			 notes = "Con este metodo conseguimos añadir los datos medicos de un Alumno.")
	@PostMapping("/alumno/{idAlumno}/datosmedicos")
	public ResponseEntity<String> aniadirDatosMedicosAlumno(@PathVariable String idAlumno, @RequestBody DatosMedicos nuevosDatosmedicos) {
		
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		if(alumnoOptional.isPresent()) {
			Alumno alumnoModificado = alumnoOptional.get();
			alumnoModificado.setDatosMedicos(nuevosDatosmedicos);
			return ResponseEntity.status(HttpStatus.OK).body(as.save(alumnoModificado).toString());
		} else {
			throw new AlumnoNoEncontradoException(idAlumno);
		}
	}
	
	/*
	 * 
	 * PUT alumno/{idAlumno}/profesor/{idProfesor}
	 * 
	 * */
	
	@ApiOperation(value = "Modificar los datos medicos de un Alumno",
			 notes = "Con este metodo conseguimos modificar los datos medicos de un Alumno.")
	@PutMapping("/alumno/{idAlumno}/datosmedicos")
	public ResponseEntity<String> modificarDatosMedicosAlumno(@PathVariable String idAlumno, @RequestBody DatosMedicos nuevosDatosmedicos) {
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		if(alumnoOptional.isPresent()) {
			Alumno alumnoModificado = alumnoOptional.get();
			alumnoModificado.setDatosMedicos(nuevosDatosmedicos);
			return ResponseEntity.status(HttpStatus.OK).body(as.edit(alumnoModificado).toString());
		} else {
			throw new AlumnoNoEncontradoException(idAlumno);
		}
	}
	
	/*
	 * 
	 * PUT alumno/{idAlumno}/profesor/{idProfesor}
	 * 
	 * */
	
	@ApiOperation(value = "Ver todos los datos medicos de un Alumno",
			 notes = "Con este metodo conseguimos ver todos los datos medicos de un Alumno.")
	@GetMapping("/alumno/{idAlumno}/datosmedicos")
	public ResponseEntity<DatosMedicos> verDatosMedicosAlumno(@PathVariable String idAlumno) {
		Optional<Alumno> alumnoOptional = as.findById(idAlumno);
		if(alumnoOptional.isPresent()) {
			Alumno alumnoModificado = alumnoOptional.get();
			DatosMedicos datos = alumnoModificado.getDatosMedicos();
			
			return ResponseEntity.status(HttpStatus.OK).body(datos);
		} else {
			throw new AlumnoNoEncontradoException(idAlumno);
		}
	}
	
}
