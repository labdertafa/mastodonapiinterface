package com.laboratorio.mastodonapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 24/07/2024
 * @updated 24/07/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
class MastodonFilterResult {
    private MastodonFilter filter;
    private String[] keyword_matches;
    private String[] status_matches;
}