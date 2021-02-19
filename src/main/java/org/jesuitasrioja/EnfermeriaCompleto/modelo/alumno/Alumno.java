package org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.profesor.Profesor;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.responsable.Responsable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Alumno")
public class Alumno implements Serializable{
	
	@Id
	@Include 
	private String identificador;
	private String dni;
	private String nombre;
	
	@Column(name = "fechaNacimiento")
	private String fechaNacimiento;
	private String telefono;
	private String direccion;
	
	@OneToMany(mappedBy = "identificador", cascade = CascadeType.ALL)
	private List<Responsable> responsables;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "tutor")
	private Profesor tutor;
}
