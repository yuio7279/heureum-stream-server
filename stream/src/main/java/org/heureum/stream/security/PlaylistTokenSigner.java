package org.heureum.stream.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

public class PlaylistTokenSigner {
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final byte[] secret;

    public PlaylistTokenSigner(String secret) {
        Objects.requireNonNull(secret, "secret");
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    public String sign(String streamId, Instant expiresAt) {
        Objects.requireNonNull(streamId, "streamId");
        Objects.requireNonNull(expiresAt, "expiresAt");
        String payload = streamId + ":" + expiresAt.getEpochSecond();
        String signature = hmac(payload);
        return payload + "." + signature;
    }

    public boolean verify(String token, Instant now) {
        Objects.requireNonNull(token, "token");
        Objects.requireNonNull(now, "now");
        String[] parts = token.split("\\.", 2);
        if (parts.length != 2) {
            return false;
        }
        String payload = parts[0];
        String expectedSignature = hmac(payload);
        if (!expectedSignature.equals(parts[1])) {
            return false;
        }
        String[] payloadParts = payload.split(":", 2);
        if (payloadParts.length != 2) {
            return false;
        }
        long expiresAt = Long.parseLong(payloadParts[1]);
        return now.getEpochSecond() <= expiresAt;
    }

    public String extractStreamId(String token) {
        Objects.requireNonNull(token, "token");
        String[] parts = token.split("\\.", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid token format.");
        }
        String[] payloadParts = parts[0].split(":", 2);
        if (payloadParts.length != 2) {
            throw new IllegalArgumentException("Invalid token payload.");
        }
        return payloadParts[0];
    }

    private String hmac(String payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to sign payload", ex);
        }
    }
}
