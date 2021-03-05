package org.jesuitasrioja.EnfermeriaCompleto.persistencia.services;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.PCR.PCR;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.repositories.PcrRepository;
import org.springframework.stereotype.Service;

@Service
public class PcrService extends BaseService<PCR, Long, PcrRepository>{

}
