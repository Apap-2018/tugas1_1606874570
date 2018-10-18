package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.JabatanPegawaiModel;
import com.apap.tugas1.repository.JabatanPegawaiDb;
@Service
@Transactional
public class JabatanPegawaiServiceImpl implements JabatanPegawaiService{
	
	@Autowired
	private JabatanPegawaiDb jabPegDb;

	@Override
	public void addJabatanPegawai(JabatanPegawaiModel jabatan) {
		jabPegDb.save(jabatan);
		
	}

	@Override
	public void deleteJabatanPegawai(JabatanPegawaiModel jabatan) {
		jabPegDb.delete(jabatan);
		
	}

	@Override
	public void updateJabatanPegawai(JabatanPegawaiModel jabatan) {
		jabPegDb.save(jabatan);
		
	}

	@Override
	public Optional<JabatanPegawaiModel> getJabatanPegawaiDetailById(Long id) {
		return jabPegDb.findById(id);
	}


}