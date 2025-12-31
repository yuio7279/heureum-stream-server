package org.heureum.common.security;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaylistTokenSignerTest {

    @Test
    void tokenIncludesStreamIdAndExpires() {
        PlaylistTokenSigner signer = new PlaylistTokenSigner("secret-key");
        Instant expiresAt = Instant.parse("2024-01-01T00:10:00Z");

        String token = signer.sign("stream-9", expiresAt);

        assertEquals("stream-9", signer.extractStreamId(token));
        assertTrue(signer.verify(token, Instant.parse("2024-01-01T00:09:59Z")));
    }

    @Test
    void expiredTokenFailsVerification() {
        PlaylistTokenSigner signer = new PlaylistTokenSigner("secret-key");
        Instant expiresAt = Instant.parse("2024-01-01T00:00:00Z");

        String token = signer.sign("stream-9", expiresAt);

        assertFalse(signer.verify(token, Instant.parse("2024-01-01T00:00:01Z")));
    }
}
