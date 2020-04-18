Prerequisites:
1. Provision an RDS instance with MySQL
2. Create following table in the database
   "create table weather.temp_look_up (zip_code varchar(10), temp varchar(10));"
   

Steps:

1. Go to WeatherDataSetupRepository java class and change database details to match your values
2. Run "mvn clean package"
3. Create Lambda function, name it "s3-event-handler2"
4. Upload your jar file
5. Configure your Lambda handler as "com.nextep.lambda.S3EventHandler::handleRequest"