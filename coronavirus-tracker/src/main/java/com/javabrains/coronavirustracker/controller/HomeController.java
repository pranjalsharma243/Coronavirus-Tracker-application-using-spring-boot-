package com.javabrains.coronavirustracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.javabrains.coronavirustracker.coronadataservice.coronavirusdataservice;
import com.javabrains.coronavirustracker.locationstats.LocationStats;

@Controller
public class HomeController {
	@Autowired
	coronavirusdataservice Coronavirusdata;
	@GetMapping("/")
	public String home(Model model)
	{
		List<LocationStats> allStats=Coronavirusdata.getAllStats();
		int totalReportedCases=allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
		
		
		model.addAttribute("locationStats",allStats);
		model.addAttribute("totalReportedCases", totalReportedCases);
		return("home");
	}

}
