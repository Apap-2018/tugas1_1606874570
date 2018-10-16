package com.apap.tugas1.repository;

import com.apap.tugas1.model.PegawaiModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PegawaiDb
 * @author debora
 */

@Repository
public interface PegawaiDb extends JpaRepository<PegawaiModel,Long> {
	PegawaiModel findByNip(String nip);
}
