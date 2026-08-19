package com.hospital.citas.Service;

import com.hospital.citas.Model.Medico;
import com.hospital.citas.Repository.MedicoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRespository MedicoRespository;

    public List<Medico> listarTodas() {
        return MedicoRespository.findAll();
    }

    public Medico guardarMedico (Medico medico){
        return MedicoRespository.save(medico);

    }

    public Medico obtenerPorId(Long id) {
        return MedicoRespository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        MedicoRespository.deleteById(id);
    }
}