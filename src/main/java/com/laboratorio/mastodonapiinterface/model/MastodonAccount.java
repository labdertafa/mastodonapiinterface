package com.laboratorio.mastodonapiinterface.model;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/07/2024
 * @updated 04/05/2025
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MastodonAccount {
    private String id;
    private String username;
    private String acct;
    private String url;
    private String display_name;
    private String note;
    private String avatar;
    private String avatar_static;
    private String header;
    private String header_static;
    private boolean locked;
    private MastodonField[] fields;
    private MastodonCustomEmoji[] emojis;
    private boolean bot;
    private boolean group;
    private boolean discoverable;
    private boolean indexable;
    // private Account moved;
    private boolean suspended;
    private boolean limited;
    private String created_at;
    private String last_status_at;
    private boolean hide_collections;
    private int statuses_count;
    private int followers_count;
    private int following_count;

    public boolean isSeguidorPotencial() {
        if (this.locked) {
            return false;
        }
        
        if (this.following_count < 2) {
            return false;
        }

        if (2 * following_count < this.followers_count) {
            return false;
        }

        if (this.last_status_at != null) {
            LocalDate ultimaActividad;
            try {
                ultimaActividad = LocalDate.parse(this.last_status_at, DateTimeFormatter.ISO_DATE);
            } catch (Exception e) {
                return false;
            }
            ReaderConfig config = new ReaderConfig("config//mastodon_api.properties");
            int maxInactividad = Integer.parseInt(config.getProperty("dias_inactividad_cuenta"));
            long nDays = ChronoUnit.DAYS.between(ultimaActividad, LocalDate.now());
            return Math.abs(nDays) <= maxInactividad;
        }
        
        return true;
    }

    public boolean isFuenteSeguidores() {
        if (this.hide_collections) {
            return false;
        }
        
        ReaderConfig config = new ReaderConfig("config//mastodon_api.properties");
        int umbral = Integer.parseInt(config.getProperty("umbral_fuente_seguidores"));
        return this.followers_count >= umbral;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.username);
        hash = 29 * hash + Objects.hashCode(this.acct);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MastodonAccount other = (MastodonAccount) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.username, other.username);
    }
}