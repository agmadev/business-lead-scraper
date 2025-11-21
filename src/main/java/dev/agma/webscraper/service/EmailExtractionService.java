package dev.agma.webscraper.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailExtractionService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    public String extractFirstCompanyEmail(String html, String domain) {
        if (html == null)
            return null;

        String lowerDomain = domain.toLowerCase();

        Matcher matcher = EMAIL_PATTERN.matcher(html);

        while (matcher.find()) {
            String email = matcher.group().toLowerCase();
            if (email.endsWith("@" + lowerDomain)) {
                return email;
            }
        }

        return null;
    }
}
