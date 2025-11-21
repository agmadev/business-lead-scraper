package dev.agma.webscraper.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.agma.webscraper.util.DomainUtil;
import dev.agma.webscraper.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GoogleMapsSearchService {

    @Value("${google.api.key}")
    private String apiKey;

    private final Gson gson = new Gson();

    public List<String> search(String query) {
        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + encoded + "&key="
                    + apiKey;

            String response = HttpClientUtil.get(url);
            if (response == null)
                return Collections.emptyList();

            JsonObject root = gson.fromJson(response, JsonObject.class);
            if (!root.has("results"))
                return Collections.emptyList();

            Set<String> domains = new HashSet<>();
            JsonArray results = root.getAsJsonArray("results");

            for (int i = 0; i < results.size(); i++) {
                JsonObject r = results.get(i).getAsJsonObject();

                if (r.has("website")) {
                    String domain = DomainUtil.extractDomain(r.get("website").getAsString());
                    if (domain != null)
                        domains.add(domain);
                }
            }

            return new ArrayList<>(domains);

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
