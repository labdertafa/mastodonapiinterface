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
public class MastodonNotification {
    private String id;
    private String type;
    private String created_at;
    private MastodonAccount account;
    private MastodonStatus status;
    private MastodonReport report;
    private MastodonRelationshipSeveranceEvent relationship_severance_event;
    private MastodonAccountWarning moderation_warning;
}