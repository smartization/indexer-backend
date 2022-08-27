package cloud.ptl.indexer.api.barcode;

import cloud.ptl.indexer.model.BarcodeEntity;
import cloud.ptl.indexer.repositories.BarcodeRepository;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import serpapi.GoogleSearch;
import serpapi.SerpApiSearchException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BarcodeService {
    private final BarcodeRepository barcodeRepository;
    @Value("${serpapi.key1}")
    private String serpKey1;
    @Value("${cache.barcode.override}")
    private boolean shouldOverride;
    @Value("${cache.barcode.ttl}")
    private Duration ttl;

    public BarcodeDTO fetch(String barcode) {
        return tryToGetFromCache(barcode).orElseGet(() -> getFromGoogle(barcode));
    }

    private BarcodeDTO getFromGoogle(String barcode) {
        log.info("Calling google for barcode: {}", barcode);
        return callGoogle(barcode);
    }

    private BarcodeDTO callGoogle(String barcode) {
        Map<String, String> parameter = new HashMap<>();

        parameter.put("api_key", serpKey1);
        parameter.put("engine", "google");
        parameter.put("q", barcode);
        parameter.put("google_domain", "google.pl");
        parameter.put("gl", "pl");
        parameter.put("hl", "pl");

        GoogleSearch search = new GoogleSearch(parameter);
        try {
            JsonObject results = search.getJson();
            BarcodeDTO barcodeDTO = new BarcodeDTO();
            barcodeDTO.setTitle(
                    results.getAsJsonArray("organic_results").get(0).getAsJsonObject().get("title").getAsString());
            barcodeDTO.setValue(barcode);
            barcodeDTO.setSearchResult(results.get("search_metadata").getAsJsonObject().get("status").getAsString());
            barcodeDTO.setProcessingTime(LocalDateTime.now());
            String stringUrl = results.getAsJsonArray("organic_results").get(0).getAsJsonObject().get("link")
                    .getAsString();
            try {
                barcodeDTO.setLink(new URL(stringUrl));
            } catch (MalformedURLException e) {
                barcodeDTO.setLink(null);
            }
            addToCache(barcodeDTO);
            return barcodeDTO;
        } catch (SerpApiSearchException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private void addToCache(BarcodeDTO barcodeDTO) {
        log.info("Saving to cache barcode: {}", barcodeDTO);
        String barcodeValue = barcodeDTO.getValue();
        boolean barcodeCached = barcodeRepository.existsByValue(barcodeValue);
        if (barcodeCached && !shouldOverride) {
            log.warn("Tried to cache existing barcode " + barcodeValue + " but overriding is disabled, not cached");
        } else {
            BarcodeEntity entity = barcodeDTO.toEntity();
            barcodeRepository.save(entity);
        }
    }

    private Optional<BarcodeDTO> tryToGetFromCache(String barcode) {
        log.info("Checking if barcode: {} is cached", barcode);
        Optional<BarcodeEntity> optionalBarcodeEntity = barcodeRepository.findByValue(barcode);
        if (optionalBarcodeEntity.isEmpty()) {
            log.info("Cache missed for barcode: {}", barcode);
        } else if (checkIfTtlExpired(optionalBarcodeEntity.get())) {
            log.info("Cache expired for barcode: {}", barcode);
            barcodeRepository.delete(optionalBarcodeEntity.get());
            optionalBarcodeEntity = Optional.empty();
        } else {
            log.info("Cache hit for {}", barcode);
        }
        BarcodeDTO barcodeDTO = optionalBarcodeEntity.map(BarcodeDTO::from).orElse(null);
        return Optional.ofNullable(barcodeDTO);
    }

    private boolean checkIfTtlExpired(BarcodeEntity barcodeEntity) {
        LocalDateTime processingTime = barcodeEntity.getProcessingTime();
        Duration timePassed = Duration.between(processingTime, LocalDateTime.now()).abs();
        Duration timeRemaining = ttl.minus(timePassed);
        log.info("Remaining TTL for barcode: {} is {}", barcodeEntity, timeRemaining);
        return timePassed.isNegative();
    }
}
