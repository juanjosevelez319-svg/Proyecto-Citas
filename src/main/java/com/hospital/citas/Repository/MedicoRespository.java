package com.hospital.citas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.citas.Model.Medico;


public interface MedicoRespository extends JpaRepository <Medico, Long> {}

