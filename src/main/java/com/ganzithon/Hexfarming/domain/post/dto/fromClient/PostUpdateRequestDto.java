package com.ganzithon.Hexfarming.domain.post.dto.fromClient;

public class PostUpdateRequestDto {
    private String title;
    private String content;
    private String ability;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
