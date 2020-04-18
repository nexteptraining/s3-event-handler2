package com.nextep.lambda;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;

public class S3EventHandler implements RequestHandler<S3EventNotification, String> {

	@Override
	public String handleRequest(S3EventNotification input, Context context) {

		context.getLogger().log("handleRequest has been called");

		S3Entity s3 = input.getRecords().get(0).getS3();
		String bucketName = s3.getBucket().getName();
		String objectKey = s3.getObject().getKey();

		context.getLogger().log("Bucket name = " + bucketName + " object key = " + objectKey);

		WeatherDataRetriever retriever = new WeatherDataRetriever();
		List<Weather> weatherObjects = Collections.emptyList();

		try {
			weatherObjects = retriever.getWeatherData(bucketName, objectKey);
			context.getLogger().log("Weather objects size =  " + weatherObjects.size());
		} catch (IOException e) {
			context.getLogger().log(e.getMessage());
		}

		WeatherDataSetupRepository repo = new WeatherDataSetupRepository();
		repo.insertWeatherData(weatherObjects);

		return new String("200 OK");
	}

}
