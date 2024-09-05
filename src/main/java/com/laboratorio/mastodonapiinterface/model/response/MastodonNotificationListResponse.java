package com.laboratorio.mastodonapiinterface.model.response;

import com.laboratorio.mastodonapiinterface.model.MastodonNotification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 25/07/2024
 * @updated 05/09/2024
 */
@Getter @Setter @AllArgsConstructor
public class MastodonNotificationListResponse {
    private String minId;
    private List<MastodonNotification> notifications;
}