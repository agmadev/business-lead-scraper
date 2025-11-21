package dev.agma.webscraper.service;

import dev.agma.webscraper.util.HttpClientUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebsiteScraperService {

    private final EmailExtractionService emailExtractionService;

    public WebsiteScraperService(EmailExtractionService emailExtractionService) {
        this.emailExtractionService = emailExtractionService;
    }

    public List<String> scrapeDomains(List<String> domains) {
        List<String> emails = new ArrayList<>();

        for (String d : domains) {
            String[] paths = { "", "/contact", "/about" };
            String base = "https://" + d;

            String found = null;

            for (String p : paths) {
                String html = HttpClientUtil.get(base + p);
                if (html != null) {
                    found = emailExtractionService.extractFirstCompanyEmail(html, d);
                    if (found != null)
                        break;
                }
            }

            if (found != null)
                emails.add(found);
        }

        return emails;
    }
}
