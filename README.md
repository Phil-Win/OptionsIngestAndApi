#Options Ingestor

This application is in two parts
1. An API that serves as a wrapper to get Stock Market Data. Current version requires you to obtain your own free API key from Tradier
    a. The API Key must be injected by via the variable tradier.access.token
    b. The port the API is deployed to is 8080 by default. Change by injecting the variable server.port
2. A Downloader service that will download Stock and options data at the time provided
    a. The job will download data into a folder /data/daily_stock_data_landing_zone on a daily basis(Future features to customize)
        i. To make the data landing folder available, you must mount a volume to the location
            E.G. -v c:/my_local_directory/daily_stock_data_landing_zone:/data/daily_stock_data_landing_zone
    b. The stocks the downloader service will take daily will be stocks defined in the variable tradier.stocks.symbols.to.download.comma.separated
        i. Inject the comma separated symbols of interest to the variable tradier.stocks.symbols.to.download.comma.separated


Features -
API to tell you if the market is open
API call to get all options chains at the time of call for a specific stock
API that gets multiple stock quotes based on comma separated stock symbols
Swagger UI can be found at http://<host>:<port>/swagger-ui.html

Downloader - Runs daily and only downloads data during times when the market is open to avoid duplicate days of download
Downloads the current stock data for all symbols mentioned and places it into a date separated folder
Downloads the entire set of the options chain for all symbols mentioned and places it into a date separated folder
Won't reach the 60 calls per minute rate limit of Tradiers Free API

Security is not implemented on this API. If you expose the port, please do so at your own risk


*** UPDATE Removed the component to make API call's to this service to make this tool more lightweight