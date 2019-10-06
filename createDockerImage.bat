gradlew.bat build

docker build -t philwin/options-ingest:0.0.1 .

docker run -p 8080:8080 -v d:/Docker/daily_stock_data_landing_zone:/data/daily_stock_data_landing_zone -d --name tradier_ingest_and_api philwin/options-ingest:0.0.1
