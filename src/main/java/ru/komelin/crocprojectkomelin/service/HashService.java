package ru.komelin.crocprojectkomelin.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class HashService {
    public String generateHash(BigDecimal bigDecimal) {
        byte[] bytes = bigDecimal.toPlainString().getBytes(StandardCharsets.UTF_8);
        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }
}
