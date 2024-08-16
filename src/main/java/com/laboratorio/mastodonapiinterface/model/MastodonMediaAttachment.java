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
 * @updated 10/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MastodonMediaAttachment {
    private String id;
    private String type;
    private String url;
    private String preview_url;
    private String remote_url;
    //private MetaData meta;
    private String description;
    private String blurhash;
}