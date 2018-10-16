package com.apap.tugas1.repository;

import com.apap.tugas1.model.InstansiModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * InstansiDb
 * @author debora
 */

@Repository
public interface InstansiDb extends JpaRepository<InstansiModel,Long>{

}
