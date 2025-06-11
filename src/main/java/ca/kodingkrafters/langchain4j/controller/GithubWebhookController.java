package ca.kodingkrafters.langchain4j.controller;

import ca.kodingkrafters.langchain4j.service.PullRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class GithubWebhookController {
    private static final Logger log = LoggerFactory.getLogger(GithubWebhookController.class);
    private final PullRequestService pullRequestService;

    public GithubWebhookController(PullRequestService pullRequestService) {
        this.pullRequestService = pullRequestService;
    }

    @PostMapping("/github")
    public ResponseEntity<Void> onPullRequestEvent(
            @RequestBody String payload,
            @RequestHeader(value = "X-GitHub-Event", required = false) String eventType
    ) {
        log.info("Received GitHub webhook: type={}, payload={}", eventType, payload);

        if ("pull_request".equals(eventType)) {
            pullRequestService.handlePrWebhook(payload);
        }

        return ResponseEntity.ok().build();
    }
}
