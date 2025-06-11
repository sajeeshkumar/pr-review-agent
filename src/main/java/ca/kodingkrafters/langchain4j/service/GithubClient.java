package ca.kodingkrafters.langchain4j.service;

import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GithubClient {
    private final GitHub gitHub;

    public GithubClient(@Value("${github.token}") String token) throws IOException {
        this.gitHub = new GitHubBuilder().withOAuthToken(token).build();
    }

    public GHPullRequest getPullRequest(String repoFullName, int prNumber) throws IOException {
        GHRepository repo = gitHub.getRepository(repoFullName);
        return repo.getPullRequest(prNumber);
    }

    public Map<String, String> getSqlFilesFromPr(GHPullRequest pr) throws IOException {
        Map<String, String> sqlFiles = new HashMap<>();

        for (GHPullRequestFileDetail file : pr.listFiles()) {
            if (file.getFilename().endsWith(".sql")) {
                String content = gitHub.getRepository(pr.getRepository().getFullName())
                        .getFileContent(file.getFilename(), pr.getHead().getSha())
                        .getContent();
                sqlFiles.put(file.getFilename(), content);
            }
        }

        return sqlFiles;
    }

    public void postCommentToPr(GHPullRequest pr, String message) throws IOException {
        pr.comment(message);
    }
}
