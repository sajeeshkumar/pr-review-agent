package ca.kodingkrafters.langchain4j.service;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SqlReviewerAgent {
    private final Assistant assistant;

    public SqlReviewerAgent(StreamingChatModel chatModel) {
        this.assistant = AiServices.builder(Assistant.class)
                .streamingChatModel(chatModel)
                .tools(List.of(new SqlBestPracticeTool(), new DataEngineeringTool(), new OrgStandardsTool()))
                .build();
    }

    public Mono<String> review(String sql) {
        return Flux.from(assistant.review(sql))
                .collectList()
                .map(chunks -> String.join("", chunks));
    }
}
