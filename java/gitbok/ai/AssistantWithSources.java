package gitbok.ai;

import dev.langchain4j.service.SystemMessage;

import dev.langchain4j.service.Result;

@SystemMessage("Always say 'Hello123!' before answering.")
public interface AssistantWithSources {
    Result<String> chat(String message);
}
