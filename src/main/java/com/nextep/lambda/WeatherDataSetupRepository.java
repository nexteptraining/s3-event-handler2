package com.nextep.lambda;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class WeatherDataSetupRepository {

	//TODO change to your values
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://weather-dev.comr1j6zhptd.us-east-2.rds.amazonaws.com:3306/weather";
	private static String userName = "admin";
	private static String password = "test1234";

	public void insertWeatherData(List<Weather> weatherData) {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(dataSource);

		template.update("delete from weather.temp_look_up");

		String sql = "INSERT INTO weather.temp_look_up VALUES (?, ?)";
		final List<Weather> wData = weatherData.subList(0, weatherData.size());
		System.out.println("wData List Size : " + wData.size());

		// performing batch insertion
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {

			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Weather weather = wData.get(i);
				// added recent
				System.out.println("*************** In S3ToMysqlLambda **********");
				System.out.println("zipCode : " + weather.getZipCode() + "\t temperature : " + weather.getTemp());
				ps.setString(1, weather.getZipCode());
				ps.setString(2, weather.getTemp());
			}

			public int getBatchSize() {
				return wData.size();
			}

		});
	}
}
