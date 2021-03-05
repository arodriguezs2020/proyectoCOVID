package org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.actuacion.Actuacion;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.Alumno;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Incidencia")
public class Incidencia implements Serializable{
	
	@Id
	@Include
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long identificador;
	private String sintomatologia;
	private Date fecha;	
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "alumno")
	private Alumno alumno;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_actuacion")
	private Actuacion actuacion;                           

}
