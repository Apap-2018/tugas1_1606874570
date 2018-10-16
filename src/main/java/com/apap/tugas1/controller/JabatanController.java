package com.apap.tugas1.controller;

import java.util.List;

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
public class JabatanController {
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value = "/jabatan", method = RequestMethod.GET)
	private String findJabatan(@RequestParam("jabatan") Long id, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(id);
		if (jabatan!=null ) {
			model.addAttribute("jabatan", jabatan);
			return "view-jabatan";
		}
		return "not-found";
	}
}
