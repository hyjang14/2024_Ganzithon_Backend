package com.ganzithon.Hexfarming.domain.post.dto.fromClient;

import com.ganzithon.Hexfarming.global.enumeration.Ability;

public class PostRequestDto {
    private String title;
    private String content;
    private Ability ability;


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

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

}
