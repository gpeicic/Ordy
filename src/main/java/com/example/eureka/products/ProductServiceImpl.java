package com.example.eureka.products;

import com.example.eureka.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    private static final double SIMILARITY_THRESHOLD = 0.7;
    private static final Set<String> CRITICAL_WORDS = Set.of(
            "zero", "light", "max", "free", "bez", "sugar"
    );

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public Long resolveProductId(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            throw new ValidationException("Naziv proizvoda je obavezan");
        }
        String normalized = normalize(rawName);
        if (normalized == null || normalized.isBlank()) {
            throw new ValidationException("Naziv proizvoda nije validan nakon normalizacije: " + rawName);
        }

        Product exact = productMapper.findByCanonicalName(normalized);
        if (exact != null) {
            return exact.getId();
        }

        Product similar = productMapper.findMostSimilar(normalized, SIMILARITY_THRESHOLD);
        if (similar != null && !hasCriticalDifference(normalized, similar.getCanonicalName())) {
            return similar.getId();
        }

        return insertNewProduct(normalized);
    }

    private Long insertNewProduct(String normalizedName) {
        Product product = new Product();
        product.setCanonicalName(normalizedName);
        productMapper.insert(product);
        return product.getId();
    }

    private String normalize(String name) {
        if (name == null) return null;

        return java.text.Normalizer.normalize(name, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private Set<String> tokenize(String name) {
        if (name == null || name.isBlank()) return Set.of();
        return Set.of(name.split(" "));
    }

    private boolean hasCriticalDifference(String a, String b) {
        Set<String> tokensA = tokenize(a);
        Set<String> tokensB = tokenize(b);

        for (String word : CRITICAL_WORDS) {
            if (tokensA.contains(word) != tokensB.contains(word)) {
                return true;
            }
        }

        Set<String> numsA = extractNumericTokens(tokensA);
        Set<String> numsB = extractNumericTokens(tokensB);

        return !numsA.equals(numsB);
    }

    private Set<String> extractNumericTokens(Set<String> tokens) {
        return tokens.stream()
                .filter(t -> t.matches(".*\\d+.*"))
                .collect(Collectors.toSet());
    }
}