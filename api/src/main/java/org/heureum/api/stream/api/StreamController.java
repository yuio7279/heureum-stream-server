package org.heureum.api.stream.api;

import org.heureum.api.stream.api.dto.StreamDtos.CreateStreamRequest;
import org.heureum.api.stream.api.dto.StreamDtos.CreateStreamResponse;
import org.heureum.api.stream.api.dto.StreamDtos.IssueWatchTokenResponse;
import org.heureum.core.domain.stream.Stream;
import org.heureum.core.domain.watch.WatchToken;
import org.heureum.core.service.stream.StreamService;
import org.heureum.core.service.watch.WatchTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/streams")
public class StreamController {

    private final StreamService streamService;
    private final WatchTokenService watchTokenService;

    public StreamController(StreamService streamService, WatchTokenService watchTokenService) {
        this.streamService = streamService;
        this.watchTokenService = watchTokenService;
    }
    /**
     * 방송 제목을 정하고 생성하기 버튼 클릭 시,
     * Stream 객체를 반환
     * streamKey를 가지고 방송 시작 할 수 있다.
     */
    @PostMapping
    public ResponseEntity<CreateStreamResponse> create(@RequestBody CreateStreamRequest req) {
        Stream stream = streamService.create(req.title());

        return ResponseEntity.ok(new CreateStreamResponse(
                stream.id().value(),
                stream.streamKey().value(),
                stream.status().name(),
                stream.createdAt()
        ));
    }

    /**
     * @param streamKey 방송을 제어 하는 열쇠
     */
    @PostMapping("/{streamKey}/start")
    public ResponseEntity<Void> start(@PathVariable String streamKey) {
        streamService.startByStreamKey(streamKey);
        return ResponseEntity.ok().build();
    }

    /**
     * @param streamKey 방송을 제어 하는 열쇄
     */
    @PostMapping("/{streamKey}/end")
    public ResponseEntity<Void> end(@PathVariable String streamKey) {
        streamService.endByStreamKey(streamKey);
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @param streamKey 방송을 제어 하는 열쇄
     * @param ttlSeconds 시청자들이 가지고 있는 토큰 만료 시간
     */
    @PostMapping("/{streamKey}/watch-token")
    public ResponseEntity<IssueWatchTokenResponse> issueWatchToken(
            @PathVariable String streamKey,
            @RequestParam(defaultValue = "60") long ttlSeconds
    ) {
        WatchToken token = watchTokenService.issue(streamKey, ttlSeconds);
        return ResponseEntity.ok(new IssueWatchTokenResponse(token.value(), token.expiresAt()));
    }
}
