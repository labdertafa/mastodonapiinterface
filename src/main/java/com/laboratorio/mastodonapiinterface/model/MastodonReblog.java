package com.laboratorio.mastodonapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 25/07/2024
 * @updated 25/07/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MastodonReblog {
    private String id;
    private String created_at;
    private String in_reply_to_id;
    private String in_reply_to_account_id;
    private boolean sensitive;
    private String spoiler_text;
    private String visibility;
    private String language;
    private String uri;
    private String url;
    private int replies_count;
    private int reblogs_count;
    private int favourites_count;
    private String edited_at;
    private boolean favourited;
    private boolean reblogged;
    private boolean muted;
    private boolean bookmarked;
    private boolean pinned;
    private String content;
    private MastodonFilterResult[] filtered;
    private MastodonApplication application;
    private MastodonAccount account;
}