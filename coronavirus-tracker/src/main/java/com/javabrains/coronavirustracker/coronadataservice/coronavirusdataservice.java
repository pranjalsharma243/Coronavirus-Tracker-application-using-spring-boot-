package com.javabrains.coronavirustracker.coronadataservice;


import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.javabrains.coronavirustracker.locationstats.LocationStats;


@Service
public class coronavirusdataservice {
	private static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	private List<LocationStats> allStats=new ArrayList<>();
	
	
	public List<LocationStats> getAllStats() {
		return allStats;
	}





	@PostConstruct
	@Scheduled(cron="* * * * * *")
	public void fetchdata() throws IOException, InterruptedException
	{
		List<LocationStats> newStats1=new ArrayList<>();
		HttpClient client=HttpClient.newHttpClient();
		HttpRequest request=HttpRequest.newBuilder()
				.uri(URI.create(VIRUS_DATA_URL))
				.build();
		HttpResponse<String> httpResponse=client.send(request,HttpResponse.BodyHandlers.ofString());
		
		StringReader csvBodyReader=new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		for (CSVRecord record : records) {
			LocationStats locationstat=new LocationStats();
			
		    locationstat.setState(record.get("Province/State"));
		    locationstat.setCountry(record.get("Country/Region"));
		    locationstat.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
		 
		    newStats1.add(locationstat);
		    
		   
		   
		}
		this.allStats=newStats1;
	}

}
