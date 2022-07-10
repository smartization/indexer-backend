package cloud.ptl.indexer.api.barcode;

import cloud.ptl.indexer.serp.OrganicResult;
import cloud.ptl.indexer.serp.SerpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import serpapi.GoogleSearch;
import serpapi.SerpApiSearchException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class BarcodeService {
    @Value("${serp.key1}")
    private String serpKey1;

    public BarcodeDTO fetch(String barcode) {
        SerpResponse response = callGoogle(barcode);
        return convertToBarcodeDTO(response);
    }

    private BarcodeDTO convertToBarcodeDTO(SerpResponse response) {
        BarcodeDTO barcodeDTO = new BarcodeDTO();
        OrganicResult organicResult = response.getOrganic_results().get(0);
        barcodeDTO.setTitle(organicResult.getTitle());
        barcodeDTO.setSearchSuccess(response.getSearch_metadata().getStatus());
        barcodeDTO.setProcessingTime(LocalDateTime.now());
        try {
            barcodeDTO.setLink(new URL(organicResult.getLink()));
        } catch (MalformedURLException e) {
            barcodeDTO.setLink(null);
        }
        return barcodeDTO;
    }

    public SerpResponse callGoogle(String barcode) {
        Map<String, String> parameter = new HashMap<>();

        parameter.put("api_key", serpKey1);
        parameter.put("engine", "google");
        parameter.put("q", barcode);
        parameter.put("google_domain", "google.pl");
        parameter.put("gl", "pl");
        parameter.put("hl", "pl");

        GoogleSearch search = new GoogleSearch(parameter);
        try {
            ObjectMapper om = new ObjectMapper();
            return om.readValue(search.getJson().toString(), SerpResponse.class);
        } catch (SerpApiSearchException | JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
