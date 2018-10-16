package com.apap.tugas1.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;

@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService{
	
	@Autowired
	private PegawaiDb pegawaiDb;

	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
		
	}

	@Override
	public void deletePegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
		
	}

	@Override
	public void updatePegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
		
	}

	@Override
	public Optional<PegawaiModel> getPegawaiDetailById(Long id) {
		return pegawaiDb.findById(id);
	}

	@Override
	public PegawaiModel getPegawaiByNip(String nip) {
		return pegawaiDb.findByNip(nip);
		
	}

	@Override
	public List<PegawaiModel> findByTahunMasukAndInstansi(String tahunMasuk, InstansiModel instansi ){
		return pegawaiDb.findByTahunMasukAndInstansi(tahunMasuk, instansi);
	}
	
	@Override
	public void deleteListElement(List<PegawaiModel> listPegawai, int tahunLahir) {
		Iterator<PegawaiModel> i = listPegawai.iterator();
		while (i.hasNext()) {
			PegawaiModel peg = i.next();
			if(Integer.parseInt(peg.getTahunLahir()) != tahunLahir) {
				i.remove();
			} 
		}
	}

}