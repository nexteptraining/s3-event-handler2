package com.nextep.lambda;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class WeatherDataRetriever {

	public List<Weather> getWeatherData(String bucketName, String objectKey) throws IOException {

		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();

		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectKey);
		S3Object s3Object = s3.getObject(getObjectRequest);
		S3ObjectInputStream s3ObjIs = s3Object.getObjectContent();

		Reader targetReader = new InputStreamReader(s3ObjIs);

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(targetReader);

		List<Weather> weatherObjects = new ArrayList<>();

		for (CSVRecord record : records) {
			String zip = record.get(0);
			String temp = record.get(1);

			Weather weatherObj = new Weather();
			weatherObj.setZipCode(zip);
			weatherObj.setTemp(temp);

			weatherObjects.add(weatherObj);
		}

		System.out.println("Weather objects size = " + weatherObjects.size());

		return weatherObjects;
	}
}
