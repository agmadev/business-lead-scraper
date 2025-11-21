package dev.agma.webscraper.util;

public class DomainUtil {

    public static String extractDomain(String url) {
        if (url == null || url.isBlank())
            return null;

        String lower = url.toLowerCase()
                .replace("https://", "")
                .replace("http://", "")
                .replace("www.", "");

        int slash = lower.indexOf('/');
        if (slash > 0) {
            lower = lower.substring(0, slash);
        }

        return lower.contains(".") ? lower.trim() : null;
    }
}
