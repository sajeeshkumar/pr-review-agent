package ca.kodingkrafters.langchain4j.service;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class SqlBestPracticeTool {
    @Tool(name = "sqlBestPracticeReview", value = "Review SQL for general SQL best practices.")
    public String review(String sql) {
        return String.format("""
            Review the following SQL code for best practices. Focus on:

            - Avoiding SELECT *
            - Index usage
            - Table and column naming
            - Normalization issues

            SQL:
            %s

            Provide clear feedback and suggestions.
        """, sql);
    }
}
