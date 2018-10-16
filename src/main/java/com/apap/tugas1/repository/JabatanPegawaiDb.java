package com.apap.tugas1.repository;

import com.apap.tugas1.model.JabatanPegawaiModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JabatanPegawaiDb
 * @author debora
 */

@Repository
public interface JabatanPegawaiDb extends JpaRepository<JabatanPegawaiModel, Long> {

}
