package com.apap.tugas1.repository;

import com.apap.tugas1.model.JabatanModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JabatanDb
 * @author debora
 */

@Repository
public interface JabatanDb extends JpaRepository<JabatanModel,Long>{

}
