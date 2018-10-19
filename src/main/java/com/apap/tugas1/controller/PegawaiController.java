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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * PegawaiController
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
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		model.addAttribute("listOfJabatan", daftarJabatan);
		model.addAttribute("listOfInstansi", daftarInstansi);
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
	private String addPegawaiSubmit(@RequestParam("instansi") String idInstansi, @RequestParam("provinsi") String idProvinsi, 
			@RequestParam("jabatan") String idJabatan, @ModelAttribute PegawaiModel pegawai, Model model) {
		System.out.println(pegawai.getNama());
		//initialisasi instansi//
		InstansiModel instansi = instansiService.findById(Long.parseLong(idInstansi));
		//initialisasi jabatan//
		List<String> idJabatanList = new ArrayList<String>(Arrays.asList(idJabatan.split(",")));
		List<JabatanModel> jabatanList = new ArrayList<JabatanModel>();
		for(String str : idJabatanList) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(str));
			jabatanList.add(jabatan);
		}
		
		//mengisi data jabatan dan isntansi//
		pegawai.setJabatanList(jabatanList);
		pegawai.setInstansi(instansi);
		
		System.out.println(pegawai.getTanggalLahirStr());
		System.out.println(instansi.getId());
		
		//mulai menyusun nip//
		String nip = instansi.getId() + pegawai.getTanggalLahirStr() + pegawai.getTahunMasuk();

		//mencari pegawai dengan tahun masuk dan instansi yang sama//
		List<PegawaiModel> anotherPegawai = pegawaiService.findByTahunMasukAndInstansi(pegawai.getTahunMasuk(), instansi);
//		for(PegawaiModel p : anotherPegawai) {
//			System.out.println(p.getNama());
//		}
		//jika tidak ada pegawai lain, nip akan ditambah dengan angka '01'//
		if(anotherPegawai.isEmpty()) {
			nip += "01";
		}
		//jika ada pegawai lain dengan instansi dan tahun masuk yang sama//
		else {
			//list pegawai akan ditrace dan buang pegawai dengan tahun lahir yang tidak sama dari list pegawai//
			pegawaiService.deleteListElement(anotherPegawai, Integer.parseInt(pegawai.getTanggalLahirStr()));
			//mengurutkan pegawai dengan tahun lahir yang sama berdasarkan nip, secara descending//
			anotherPegawai.sort(new Comparator<PegawaiModel>() {
			    @Override
			    public int compare(PegawaiModel m1, PegawaiModel m2) {
			    	return m2.getNip().compareTo(m1.getNip());
			     }
			});
			System.out.println(anotherPegawai.get(0).getNip());
			/*mengambil nip dari pegawai teratas, dan mengambil nipnya, lalu tambahkan 1 dari nip tersebut dan
			ambil 2 digit terakhir dari nip yang sudah ditambahkan, untuk ditaruh di nip pegawai bar */
			Long number = Long.parseLong(anotherPegawai.get(0).getNip());
			number += 1;
			nip += Long.toString(number).substring(14);
		}
		
		pegawai.setNip(nip);
		System.out.println(pegawai.toString());
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("nip", nip);
		return "addPegawai-success";
	}
	

	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.GET)
	private String updatePegawai(@PathVariable(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("newPegawai", new PegawaiModel());
		
		List<ProvinsiModel> daftarProv = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProv);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
	    
		return "updatePegawai";
	}

	//update pegawai//
	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.POST)
	private String updatePegawai(@ModelAttribute PegawaiModel newPegawai, @RequestParam("instansiA") String idInstansi, @RequestParam("provinsi") String idProvinsi, 
			@RequestParam("jabatan") String idJabatan, @PathVariable(value = "nip") String nip, Model model) {
		System.out.println(nip);
		//initialisasi instansi//
		InstansiModel newInstansi = instansiService.findById(Long.parseLong(idInstansi));
		System.out.println(newInstansi.getNama());
		//initialisasi jabatan//
		List<String> newIdJabatanList = new ArrayList<String>(Arrays.asList(idJabatan.split(",")));
		List<JabatanModel> newJabatanList = new ArrayList<JabatanModel>();
		for(String str : newIdJabatanList) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(str));
			newJabatanList.add(jabatan);
		}
		
		//mengisi data jabatan dan isntansi//
		newPegawai.setJabatanList(newJabatanList);
		newPegawai.setInstansi(newInstansi);
		System.out.println(newPegawai.getInstansi().getNama()+"ini");
		//mulai menyusun nip//
		String newNip = newInstansi.getId() + newPegawai.getTanggalLahirStr() + newPegawai.getTahunMasuk();
		
		//mencari pegawai dengan tahun masuk dan instansi yang sama//
		List<PegawaiModel> newAnotherPegawai = pegawaiService.findByTahunMasukAndInstansi(newPegawai.getTahunMasuk(), newInstansi);
		/**
		 * for(PegawaiModel p : anotherPegawai) {
		 * System.out.println(p.getNama());
		 * }
		 */
		//jika tidak ada pegawai lain, nip akan ditambah dengan angka '01'//
		if(newAnotherPegawai.isEmpty()) {
			newNip += "01";
		}
		//jika ada pegawai lain dengan instansi dan tahun masuk yang sama//
		else {
			//list pegawai akan ditrace dan buang pegawai dengan tahun lahir yang tidak sama dari list pegawai//
			pegawaiService.deleteListElement(newAnotherPegawai, Integer.parseInt(newPegawai.getTahunLahir()));
			//mengurutkan pegawai dengan tahun lahir yang sama berdasarkan nip, secara descending//
			newAnotherPegawai.sort(new Comparator<PegawaiModel>() {
			    @Override
			    public int compare(PegawaiModel m1, PegawaiModel m2) {
			    	return m2.getNip().compareTo(m1.getNip());
			     }
			});
			System.out.println(newAnotherPegawai.get(0).getNip());
			/*mengambil nip dari pegawai teratas, dan mengambil nipnya, lalu tambahkan 1 dari nip tersebut dan
			ambil 2 digit terakhir dari nip yang sudah ditambahkan, untuk ditaruh di nip pegawai bar */
			Long number = Long.parseLong(newAnotherPegawai.get(0).getNip());
			number += 1;
			newNip += Long.toString(number).substring(14);
		}
		
		newPegawai.setNip(newNip);
		PegawaiModel p = pegawaiService.getPegawaiByNip(nip);
		System.out.println(p.getNip()+p.getNama());
		pegawaiService.updatePegawai(nip, newPegawai);
		model.addAttribute("nip", newNip);
		return "updatePegawai-success";
	}
	
	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
	private String findPegawai(Model model) {
		List<ProvinsiModel> daftarProv = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProv);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
		return "cari-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/cari", params = {"cari"}, method = RequestMethod.POST)
	private String findPegawaiCari(@ModelAttribute PegawaiModel newPegawai, @RequestParam("instansi") String idInstansi, @RequestParam("provinsi") String idProvinsi, 
			@RequestParam("jabatan") String idJabatan, Model model) {
		InstansiModel instansi = instansiService.findById(Long.parseLong(idInstansi));
		JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(idJabatan));
		List<PegawaiModel> listPegawai = pegawaiService.getFilter(idInstansi, idJabatan);
		System.out.println(listPegawai);
		model.addAttribute("nama", instansi.getNama());
		model.addAttribute("namaJabatan", jabatan.getNama());
	    model.addAttribute("listPegawai", listPegawai);
	    
	    List<ProvinsiModel> daftarProv = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProv);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
	    
		return "cari-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/termuda-tertua/", method = RequestMethod.GET)
	private String mudatua(@RequestParam("instansi") Long id, @ModelAttribute PegawaiModel newPegawai, Model model) {
		InstansiModel instansi = instansiService.findById(id);
		List<PegawaiModel> listPegawai = instansi.getPegawaiInstansi();
		listPegawai.sort(new Comparator<PegawaiModel>() {
		    @Override
		    public int compare(PegawaiModel m1, PegawaiModel m2) {
		    	return m2.getTanggalLahir().compareTo(m1.getTanggalLahir());
		     }
		});
		PegawaiModel pegawaiTertua = listPegawai.get(listPegawai.size()-1);
		String namaInstansiTertua = pegawaiTertua.getInstansi().getNama();
		String namaProvinsiTertua = pegawaiTertua.getInstansi().getProvinsi().getNama();
		List<JabatanModel> listJabatanTertua = pegawaiTertua.getJabatanList();  
		model.addAttribute("pegawaiTertua",pegawaiTertua);
		model.addAttribute("namaInstansiTertua",namaInstansiTertua);
		model.addAttribute("namaProvinsiTertua",namaProvinsiTertua);
		model.addAttribute("listJabatanTertua",listJabatanTertua);
		
		PegawaiModel pegawaiTermuda = listPegawai.get(0);
		String namaInstansiTermuda = pegawaiTermuda.getInstansi().getNama();
		String namaProvinsiTermuda = pegawaiTermuda.getInstansi().getProvinsi().getNama();
		List<JabatanModel> listJabatanTermuda = pegawaiTermuda.getJabatanList();
		model.addAttribute("pegawaiTermuda",pegawaiTermuda);
		model.addAttribute("namaInstansiTermuda",namaInstansiTermuda);
		model.addAttribute("namaProvinsiTermuda",namaProvinsiTermuda);
		model.addAttribute("listJabatanTermuda",listJabatanTermuda);
		
		return "tua-muda";
	}
	
}
