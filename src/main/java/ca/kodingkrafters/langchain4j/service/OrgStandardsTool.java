package ca.kodingkrafters.langchain4j.service;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class OrgStandardsTool {
    @Tool(name = "orgStandardsReview", value = "Review SQL for organization-specific standards.")
    public String review(String sql) {
        return String.format("""
            You are reviewing SQL for a specific organization. Check the following:

            - Column naming conventions (e.g., snake_case)
            - Table naming patterns (e.g., names ending in _tbl)
            - Presence of SQL comments or documentation
            - Compliance with org-specific design rules (e.g., audit columns, history tables)

            SQL:
            %s

            Highlight any violations or recommend changes.
        """, sql);
    }
}
