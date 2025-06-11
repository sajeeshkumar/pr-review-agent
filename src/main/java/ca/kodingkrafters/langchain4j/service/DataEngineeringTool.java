package ca.kodingkrafters.langchain4j.service;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class DataEngineeringTool {
    @Tool(name = "dataEngineeringReview", value = "Review SQL for data engineering best practices.")
    public String review(String sql) {
        return String.format("""
            You are a data engineer. Review the SQL for:
            - Partitioning and clustering strategies
            - Scalability and performance implications
            - Safety in data pipelines (e.g., idempotency, failure recovery)
            - Audit trail and metadata tracking
            
            SQL:
            %s
            
            Provide clear and actionable feedback based on modern data engineering principles.
        """, sql);
    }
}
