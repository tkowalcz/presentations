package pl.codewise.hyperion.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    private String content;

    public Response() {
        // Jackson deserialization
    }

    public Response(String content) {
        this.content = content;
    }

    @JsonProperty
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}