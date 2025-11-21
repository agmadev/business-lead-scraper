# Business Lead Scraper

Automated Java-based scraper that discovers businesses through Google Maps search queries, extracts their website domains, crawls key pages, detects company email addresses, and appends unique leads into Google Sheets. Designed for daily lead generation and outreach automation.

## Features
- Google Maps Text Search integration  
- Extracts business website domains  
- Scrapes homepage, contact, and about pages  
- Finds the first valid company email  
- Automatically writes to Google Sheets  
- Built-in deduplication (no duplicate emails)  
- Daily scheduled scraping  
- Docker-ready  
- Fully standalone

## Requirements
- Java 21  
- Maven  
- Google Cloud API key  
- Google Service Account JSON  
- Google Sheet ID  

## Environment Variables
GOOGLE_API_KEY=your_api_key  
GOOGLE_SHEET_ID=your_sheet_id  
GOOGLE_CREDENTIALS=your_service_account_json  

Spring Boot config (application.properties):

google.api.key=${GOOGLE_API_KEY}  
google.sheet.id=${GOOGLE_SHEET_ID}  
google.credentials=${GOOGLE_CREDENTIALS}  

## Run Locally
Build:  
mvn clean install -DskipTests  

Run:  
java -jar target/*.jar  

## Docker
Build image:  
docker build -t business-lead-scraper .  

Run container:  
docker run -d --name business-lead-scraper --env-file .env business-lead-scraper  

## Scheduler
Runs automatically every day at 09:00 UTC.

Daily workflow:  
1. Perform 5 Google Maps searches  
2. Extract business websites  
3. Scrape homepage/contact/about pages  
4. Detect company emails  
5. Deduplicate  
6. Append new emails to Google Sheet  

## Deduplication
Before inserting:
- Reads entire email column  
- Normalizes all emails  
- Skips duplicates  
- Inserts only new unique emails  
