package ca.kodingkrafters.langchain4j.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kohsuke.github.GHPullRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class PullRequestService {
    private static final Logger log = LoggerFactory.getLogger(PullRequestService.class);

    private final GithubClient gitHubClient;
    private final SqlReviewerAgent sqlReviewerAgent;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PullRequestService(GithubClient gitHubClient, SqlReviewerAgent sqlReviewerAgent) {
        this.gitHubClient = gitHubClient;
        this.sqlReviewerAgent = sqlReviewerAgent;
    }


    public void handlePrWebhook(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            String repoFullName = root.path("repository").path("full_name").asText();
            int prNumber = root.path("pull_request").path("number").asInt();

            GHPullRequest pr = gitHubClient.getPullRequest(repoFullName, prNumber);
            Map<String, String> sqlFiles = gitHubClient.getSqlFilesFromPr(pr);

            sqlFiles.forEach((filename, content) -> {
                sqlReviewerAgent.review(content)
                        .doOnNext(review -> {
                            try {
                                gitHubClient.postCommentToPr(pr, review);
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        })
                        .doOnError(error -> log.error("Error reviewing SQL file {}: {}", filename, error.getMessage()))
                        .subscribe();
            });

        } catch (Exception e) {
            log.error("Error handling webhook", e);
        }
    }
}
