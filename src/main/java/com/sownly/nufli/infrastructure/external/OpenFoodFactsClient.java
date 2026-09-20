package com.sownly.nufli.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sownly.nufli.domain.nutrition.Food;
import com.sownly.nufli.domain.nutrition.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OpenFoodFactsClient {

    private static final Logger log = LoggerFactory.getLogger(OpenFoodFactsClient.class);
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final FoodRepository foodRepository;

    public OpenFoodFactsClient(ObjectMapper objectMapper, FoodRepository foodRepository) {
        this.objectMapper = objectMapper;
        this.foodRepository = foodRepository;
        this.restClient = RestClient.builder()
            .baseUrl("https://world.openfoodfacts.net")
            .defaultHeader("User-Agent", "NuFli-NutritionApp/1.0 (contact@sownly.com)")
            .build();
    }

    public List<Food> searchAndCacheFoods(String query) {
        List<Food> savedFoods = new ArrayList<>();
        try {
            String response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/v2/search")
                    .queryParam("q", query)
                    .queryParam("fields", "product_name,brands,nutriments")
                    .queryParam("page_size", 10)
                    .build())
                .retrieve()
                .body(String.class);

            if (response == null || response.isBlank()) {
                return savedFoods;
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode products = root.path("products");
            if (products.isArray()) {
                for (JsonNode prod : products) {
                    String name = prod.path("product_name").asText("").trim();
                    if (name.isBlank()) continue;

                    String brand = prod.path("brands").asText("").trim();
                    JsonNode nutriments = prod.path("nutriments");

                    double cals = nutriments.path("energy-kcal_100g").asDouble(-1.0);
                    if (cals < 0) {
                        double kj = nutriments.path("energy_100g").asDouble(0.0);
                        cals = kj > 0 ? kj / 4.184 : 0.0;
                    }
                    double pro = Math.max(0.0, nutriments.path("proteins_100g").asDouble(0.0));
                    double carbs = Math.max(0.0, nutriments.path("carbohydrates_100g").asDouble(0.0));
                    double fat = Math.max(0.0, nutriments.path("fat_100g").asDouble(0.0));
                    double fiber = Math.max(0.0, nutriments.path("fiber_100g").asDouble(0.0));

                    Food food = new Food(
                        null,
                        name,
                        brand.isBlank() ? null : brand,
                        BigDecimal.valueOf(Math.round(cals * 100.0) / 100.0),
                        BigDecimal.valueOf(Math.round(pro * 100.0) / 100.0),
                        BigDecimal.valueOf(Math.round(carbs * 100.0) / 100.0),
                        BigDecimal.valueOf(Math.round(fat * 100.0) / 100.0),
                        BigDecimal.valueOf(Math.round(fiber * 100.0) / 100.0),
                        true
                    );
                    savedFoods.add(foodRepository.save(food));
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch foods from Open Food Facts for query '{}': {}", query, e.getMessage());
        }
        return savedFoods;
    }
}
