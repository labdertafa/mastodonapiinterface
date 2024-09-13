package com.laboratorio.mastodonapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.mastodonapiinterface.MastodonNotificationApi;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.model.MastodonNotification;
import com.laboratorio.mastodonapiinterface.model.response.MastodonNotificationListResponse;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 25/07/2024
 * @updated 05/09/2024
 */
public class MastodonNotificationApiImpl extends MastodonBaseApi implements MastodonNotificationApi {
    public MastodonNotificationApiImpl(String accessToken) {
        super(accessToken);
    }
    
    @Override
    public MastodonNotificationListResponse getAllNotifications() throws Exception {
        return this.getAllNotifications(0);
    }
    
    @Override
    public MastodonNotificationListResponse getAllNotifications(int limit) throws Exception {
        return this.getAllNotifications(limit, 0);
    }

    @Override
    public MastodonNotificationListResponse getAllNotifications(int limit, int quantity) throws Exception {
        return this.getAllNotifications(limit, quantity, null);
    }
    
    // Función que devuelve una página de notificaciones de una cuenta
    private MastodonNotificationListResponse getNotificationPage(String url, int limit, int okStatus, String posicionInicial) throws Exception {
        Client client = ClientBuilder.newClient();
        Response response = null;
        
        try {
            WebTarget target = client.target(url)
                    .queryParam("limit", limit)
                    .queryParam("min_id", posicionInicial);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new MastondonApiException(MastodonNotificationApiImpl.class.getName(), str);
            }
            
            Gson gson = new Gson();
            String minId = posicionInicial;
            List<MastodonNotification> notifications = gson.fromJson(jsonStr, new TypeToken<List<MastodonNotification>>(){}.getType());
            if (!notifications.isEmpty()) {
                log.debug("Se ejecutó la query: " + url);
                log.debug("Resultados encontrados: " + notifications.size());

                String linkHeader = response.getHeaderString("link");
                log.debug("Recibí este link: " + linkHeader);
                minId = this.extractMinId(linkHeader);
                log.debug("Valor del min_id: " + minId);
            }

            // return accounts;
            return new MastodonNotificationListResponse(minId, notifications);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (MastondonApiException e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }

    @Override
    public MastodonNotificationListResponse getAllNotifications(int limit, int quantity, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getNotifications_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getNotifications_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getNotifications_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getNotifications_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        List<MastodonNotification> notifications = null;
        boolean continuar = true;
        String min_id = "0";
        if (posicionInicial != null) {
            min_id = posicionInicial;
        }
        
        if (quantity > 0) {
            usedLimit = Math.min(usedLimit, quantity);
        }
        
        try {
            do {
                MastodonNotificationListResponse notificationListResponse = this.getNotificationPage(endpoint, usedLimit, okStatus, min_id);
                if (notifications == null) {
                    notifications = notificationListResponse.getNotifications();
                } else {
                    notifications.addAll(notificationListResponse.getNotifications());
                }
                
                min_id = notificationListResponse.getMinId();
                log.debug("getFollowers. Cantidad: " + quantity + ". Recuperados: " + notifications.size() + ". Min_id: " + min_id);
                if (notificationListResponse.getNotifications().isEmpty()) {
                    continuar = false;
                } else {
                    if (quantity > 0) {
                        if (notifications.size() >= quantity) {
                            continuar = false;
                        }
                    } else {
                        if (notificationListResponse.getNotifications().size() < usedLimit) {
                            continuar = false;
                        }
                    }
                }
            } while (continuar);

            if (quantity == 0) {
                return new MastodonNotificationListResponse(min_id, notifications);
            }
            
            return new MastodonNotificationListResponse(min_id, notifications.subList(0, Math.min(quantity, notifications.size())));
        } catch (Exception e) {
            throw e;
        }
    }
}