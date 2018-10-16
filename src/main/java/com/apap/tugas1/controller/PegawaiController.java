package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.*;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * PilotController
 * @author debora
 */

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		model.addAttribute("listOfJabatan", daftarJabatan);
		return "HomePage";
	}
	
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String findPegawai(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		if (pegawai!=null ) {
			model.addAttribute("pegawai", pegawai);
			return "view-pegawai";
		}
		return "not-found";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("pegawai", new PegawaiModel());
		
		List<ProvinsiModel> daftarProv = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProv);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
		return "addPegawai";
	}

	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegawaiSubmit(@RequestParam("instansi") String idInstansi, @RequestParam("provinsi") String idProvinsi, @RequestParam("jabatan") String idJabatan, @ModelAttribute PegawaiModel pegawai, Model model) {
		System.out.println(pegawai.getNama());
		InstansiModel instansiRandom = instansiService.findById(Long.parseLong(idInstansi));
		String namaInstansi = instansiRandom.getNama();
		
		ProvinsiModel prov = provinsiService.getProvinsiDetailById(Integer.parseInt(idProvinsi));
		InstansiModel instansi = instansiService.getInstansiByNamaAndProvinsi(namaInstansi, prov);
		
		List<String> idJabatanList = new ArrayList<String>(Arrays.asList(idJabatan.split(",")));
		List<JabatanModel> jabatanList = new ArrayList<JabatanModel>();
		for(String str : idJabatanList) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(str));
			jabatanList.add(jabatan);
		}
		
		pegawai.setJabatanList(jabatanList);
		pegawai.setInstansi(instansi);
		
		System.out.println(pegawai.getTanggalLahirStr());
		System.out.println(instansi.getId());
		List<PegawaiModel> anotherPegawai = pegawaiService.findByTahunMasukAndInstansi(pegawai.getTahunMasuk(), instansi);
		for(PegawaiModel p : anotherPegawai) {
			System.out.println(p.getNama());
		}
		String nip = instansi.getId() + pegawai.getTanggalLahirStr() + pegawai.getTahunMasuk();
		if(anotherPegawai.isEmpty()) {
			nip += "01";
		}
		else {
			pegawaiService.deleteListElement(anotherPegawai, Integer.parseInt(pegawai.getTahunLahir()));
			anotherPegawai.sort(new Comparator<PegawaiModel>() {
			    @Override
			    public int compare(PegawaiModel m1, PegawaiModel m2) {
			    	return m2.getNip().compareTo(m1.getNip());
			     }
			});
			System.out.println(anotherPegawai.get(0).getNip());
			Long number = Long.parseLong(anotherPegawai.get(0).getNip());
			number += 1;
			nip += Long.toString(number).substring(14);
		}
		
		pegawai.setNip(nip);
		System.out.println(pegawai.toString());
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("nip", nip);
		return "add";
	}
}
