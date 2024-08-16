package com.laboratorio.mastodonapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/07/2024
 * @updated 10/07/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MastodonField {
    private String name;
    private String value;
    private String verified_at;
}
