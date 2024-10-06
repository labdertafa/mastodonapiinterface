package com.laboratorio.api;

import com.laboratorio.clientapilibrary.exceptions.ApiClientException;
import com.laboratorio.mastodonapiinterface.MastodonNotificationApi;
import com.laboratorio.mastodonapiinterface.impl.MastodonNotificationApiImpl;
import com.laboratorio.mastodonapiinterface.model.response.MastodonNotificationListResponse;
import com.laboratorio.mastodonapiinterface.utils.MastodonApiConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 25/07/2024
 * @updated 06/10/2024
 */
public class MastodonNotificationApiTest {
    private String accessToken;
    private MastodonNotificationApi notificationApi;
    
    @BeforeEach
    private void initNotificationApi() {
        this.accessToken = MastodonApiConfig.getInstance().getProperty("access_token");
        String urlBase = MastodonApiConfig.getInstance().getProperty("instancia_test");
        this.notificationApi = new MastodonNotificationApiImpl(urlBase, this.accessToken);
    }
    
    @Test
    public void get20Notifications() throws Exception { // Con default limit
        int cantidad  = 20;
        
        MastodonNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(0, cantidad);

        assertEquals(cantidad, notificationListResponse.getNotifications().size());
        assertTrue(notificationListResponse.getMinId() != null);
    }
    
    @Test
    public void get20NotificationsWithLimit() throws Exception { // Con limit
        int cantidad  = 20;
        int limit = 50;
        
        MastodonNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(limit, cantidad);

        assertEquals(cantidad, notificationListResponse.getNotifications().size());
        assertTrue(notificationListResponse.getMinId() != null);
    }
    
    @Test
    public void get110Notifications() throws Exception {
        int cantidad  = 110;
        
        MastodonNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(0, cantidad);

        assertEquals(cantidad, notificationListResponse.getNotifications().size());
        assertTrue(notificationListResponse.getMinId() != null);
    }
    
    @Test
    public void getAllNotifications() throws Exception {
        MastodonNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(80);

        assertTrue(notificationListResponse.getNotifications().size() > 20);
        assertTrue(notificationListResponse.getMinId() != null);
    }
    
    @Test
    public void getNotificationError() {
        String urlBase = MastodonApiConfig.getInstance().getProperty("instancia_test");
        
        this.notificationApi = new MastodonNotificationApiImpl(urlBase, "INVALID_TOKEN");

        assertThrows(ApiClientException.class, () -> {
            this.notificationApi.getAllNotifications();
        });
    }
    
    @Test
    public void getNotificationsWithSinceId() throws Exception {
        String sinceId = "312961068";

        MastodonNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(0, 0, sinceId);

        assertTrue(notificationListResponse.getNotifications().size() >= 0);
        assertTrue(notificationListResponse.getMinId() != null);
    }
}