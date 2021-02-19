package org.jesuitasrioja.EnfermeriaCompleto.modelo.responsable;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "Responsable")
public class Responsable implements Serializable{
	@Id
	@Include
	private String identificador;
	private String telefono;
	private String parentesco;
	private String nombre;
}
