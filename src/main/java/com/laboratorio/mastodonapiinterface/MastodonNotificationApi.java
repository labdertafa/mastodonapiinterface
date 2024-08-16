package com.laboratorio.mastodonapiinterface;

import com.laboratorio.mastodonapiinterface.model.response.MastodonNotificationListResponse;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 25/07/2024
 * @updated 16/08/2024
 */
public interface MastodonNotificationApi {
    // Obtiene las notificaciones del usuario. Quantity indica el número de registros a recuperar (cero significa todos). Se puede indicar una posición inicial (en su ausencia se asume que es nula).
    MastodonNotificationListResponse getAllNotifications() throws Exception;
    MastodonNotificationListResponse getAllNotifications(int limit) throws Exception;
    MastodonNotificationListResponse getAllNotifications(int limit, int quantity) throws Exception;
    MastodonNotificationListResponse getAllNotifications(int limit, int quantity, String posicionInicial) throws Exception;
}