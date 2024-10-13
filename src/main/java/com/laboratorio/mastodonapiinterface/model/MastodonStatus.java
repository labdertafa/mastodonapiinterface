package com.laboratorio.mastodonapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 24/07/2024
 * @updated 13/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MastodonStatus {
    private String id;
    private String uri;
    private String created_at;
    private MastodonAccount account;
    private String content;
    private String visibility;
    private boolean sensitive;
    private String spoiler_text;
    private MastodonMediaAttachment[] media_attachments;
    private MastodonApplication application;
    private MastodonMention[] mentions;
    private MastodonStatusTag[] tags;
    private MastodonCustomEmoji[] emojis;
    private int reblogs_count;
    private int favourites_count;
    private int replies_count;
    private String url;
    private String in_reply_to_id;
    private String in_reply_to_account_id;
    private MastodonReblog reblog;
    private String language;
    private String text;
    private String edited_at;
    private boolean favourited;
    private boolean reblogged;
    private boolean muted;
    private boolean bookmarked;
    private boolean pinned;
    private MastodonFilterResult[] filtered;

    @Override
    public String toString() {
        return "MastodonStatus{" + "id=" + id + ", created_at=" + created_at + ", content=" + content + ", reblogs_count=" + reblogs_count + ", favourites_count=" + favourites_count + ", replies_count=" + replies_count + ", language=" + language + '}';
    }
}