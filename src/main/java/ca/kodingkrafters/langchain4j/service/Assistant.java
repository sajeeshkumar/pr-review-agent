package ca.kodingkrafters.langchain4j.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService
public interface Assistant {
    @SystemMessage("""
    You are a SQL Expert. You must review the following SQL content using three tools **in this exact order**:
    
    1. SQL Best Practices  
    2. Data Engineering Practices  
    3. Organization-Specific Standards

    Always Respond in the following format:
    ###  SQL Best Practices
    ...
            
    ###  Data Engineering
    ...
            
    ### Org Standards
    ...
    """)
    Flux<String> review(String sql);
}
