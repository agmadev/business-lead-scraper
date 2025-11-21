package dev.agma.webscraper.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.*;

@Service
public class GoogleSheetsService {

    @Value("${google.sheet.id}")
    private String sheetId;

    @Value("${google.credentials}")
    private String credsJson;

    private Sheets getSheetsClient() {
        try {
            ServiceAccountCredentials creds = (ServiceAccountCredentials) ServiceAccountCredentials.fromStream(
                    new ByteArrayInputStream(credsJson.getBytes()))
                    .createScoped(Set.of("https://www.googleapis.com/auth/spreadsheets"));

            return new Sheets.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(creds))
                    .setApplicationName("Webscraper")
                    .build();

        } catch (Exception e) {
            return null;
        }
    }

    public void appendEmails(List<String> emails) {
        try {
            Sheets sheets = getSheetsClient();
            if (sheets == null)
                return;

            ValueRange existing = sheets.spreadsheets().values()
                    .get(sheetId, "A:A")
                    .execute();

            HashSet<String> existingSet = new HashSet<>();

            if (existing.getValues() != null) {
                for (List<Object> row : existing.getValues()) {
                    if (!row.isEmpty())
                        existingSet.add(row.get(0).toString().trim().toLowerCase());
                }
            }

            List<List<Object>> rows = new ArrayList<>();

            for (String email : emails) {
                String cleaned = email.trim().toLowerCase();
                if (!existingSet.contains(cleaned)) {
                    rows.add(Collections.singletonList(cleaned));
                    existingSet.add(cleaned);
                }
            }

            if (!rows.isEmpty()) {
                ValueRange body = new ValueRange().setValues(rows);

                sheets.spreadsheets().values()
                        .append(sheetId, "A:A", body)
                        .setValueInputOption("RAW")
                        .execute();
            }

        } catch (Exception ignored) {
        }
    }
}
