package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan (JabatanModel jabatan);
	void deleteJabatan (JabatanModel jabatan);
	void updateJabatan (JabatanModel jabatan);
	JabatanModel getJabatanDetailById(Long id);
	List<JabatanModel> findAllJabatan();
	void updateJabatan(Long id_jabatan, JabatanModel jabatan);
}