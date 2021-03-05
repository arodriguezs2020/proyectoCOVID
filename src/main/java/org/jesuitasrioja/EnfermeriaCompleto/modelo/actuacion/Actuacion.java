package org.jesuitasrioja.EnfermeriaCompleto.modelo.actuacion;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.PCR.PCR;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.Incidencia;

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
@Table(name = "Actuacion")
public class Actuacion implements Serializable{
	
	@Id
	@Include
	private String identificador;
	private Date fecha;
	private String ultimoDiaClase;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_indicencia")
	private Incidencia incidencia;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "identificador")
	private List<PCR> listaPCR;

}
