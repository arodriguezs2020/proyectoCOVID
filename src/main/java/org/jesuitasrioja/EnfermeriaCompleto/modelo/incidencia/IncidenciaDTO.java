package org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidenciaDTO implements Serializable{

	private String sintomatologia;
	private Date fecha;

}
