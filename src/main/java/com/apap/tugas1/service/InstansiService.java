package com.apap.tugas1.service;

import java.util.Optional;

import com.apap.tugas1.model.InstansiModel;

public interface InstansiService {
	void addInstansi (InstansiModel instansi);
	void deleteInstansi (InstansiModel instansi);
	void updateInstansi (InstansiModel instansi);
	Optional<InstansiModel> getInstansiDetailById(Long id);
}