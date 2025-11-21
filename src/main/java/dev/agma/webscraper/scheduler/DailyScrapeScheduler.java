package dev.agma.webscraper.scheduler;

import dev.agma.webscraper.service.GoogleMapsSearchService;
import dev.agma.webscraper.service.GoogleSheetsService;
import dev.agma.webscraper.service.WebsiteScraperService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyScrapeScheduler {

    private final GoogleMapsSearchService googleMapsSearchService;
    private final WebsiteScraperService websiteScraperService;
    private final GoogleSheetsService googleSheetsService;

    public DailyScrapeScheduler(GoogleMapsSearchService googleMapsSearchService,
            WebsiteScraperService websiteScraperService,
            GoogleSheetsService googleSheetsService) {
        this.googleMapsSearchService = googleMapsSearchService;
        this.websiteScraperService = websiteScraperService;
        this.googleSheetsService = googleSheetsService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void runDaily() {
        String[] queries = {
                "marketing agencies united states",
                "recruiting agencies united states",
                "ecommerce brands united states",
                "accounting bookkeeping firms united states",
                "logistics delivery companies united states"
        };

        for (String q : queries) {
            List<String> domains = googleMapsSearchService.search(q);
            List<String> emails = websiteScraperService.scrapeDomains(domains);
            googleSheetsService.appendEmails(emails);
        }
    }
}
