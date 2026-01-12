package com.example.stock.integration;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import java.util.List;
import java.util.Arrays;
import com.example.stock.dto.menuitemssnapshot.PosMenuItemDTO;
@Component
@RequiredArgsConstructor
public class PosStockClient {
    private final RestTemplate restTemplate = new RestTemplate();
    public List<PosMenuItemDTO> fetchMenuItems(String key) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Stock-Key", key);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PosMenuItemDTO[]> response =
                restTemplate.exchange(
                        "http://localhost:8000/api/integrations/stock/menu-items",
                        HttpMethod.GET,
                        request,
                        PosMenuItemDTO[].class
                );
        return Arrays.asList(response.getBody());
    }


}
