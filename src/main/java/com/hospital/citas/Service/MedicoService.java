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
    // Método para listar todos los médicos
    public List<Medico> listarTodas() {
        return MedicoRespository.findAll();
    }
    // Método para guardar un médico
    public Medico guardarMedico (Medico medico){
        return MedicoRespository.save(medico);

    }
    // Método para obtener un médico por su ID
    public Medico obtenerPorId(Long id) {
        return MedicoRespository.findById(id).orElse(null);
    }
    // Método para eliminar un médico por su ID
    public void eliminar(Long id) {
        MedicoRespository.deleteById(id);
    }
}