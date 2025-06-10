package gitbok.ai;

import dev.langchain4j.service.SystemMessage;

import dev.langchain4j.service.Result;

public interface AssistantWithSources {
    Result<String> chat(String message);
}
