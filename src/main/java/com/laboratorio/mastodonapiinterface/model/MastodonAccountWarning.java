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
public class MastodonAccountWarning {
    private String id;
    private String action;
    private String text;
    private String[] status_ids;
    private MastodonAccount target_account;
    private MastodonAppeal appeal;
    private String created_at;
}