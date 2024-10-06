package com.laboratorio.api;

import com.laboratorio.clientapilibrary.exceptions.ApiClientException;
import com.laboratorio.mastodonapiinterface.MastodonStatusApi;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.impl.MastodonStatusApiImpl;
import com.laboratorio.mastodonapiinterface.model.MastodonAccount;
import com.laboratorio.mastodonapiinterface.model.MastodonMediaAttachment;
import com.laboratorio.mastodonapiinterface.model.MastodonStatus;
import com.laboratorio.mastodonapiinterface.utils.MastodonApiConfig;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 24/07/2024
 * @updated 06/10/2024
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MastodonStatusApiTest {
    private String accessToken;
    private MastodonStatusApi statusApi;
    private static String idElim = "";
    private static int operationCount;
    
    @BeforeEach
    private void initTests() {
        this.accessToken = MastodonApiConfig.getInstance().getProperty("access_token");
        String urlBase = MastodonApiConfig.getInstance().getProperty("instancia_test");
        this.statusApi = new MastodonStatusApiImpl(urlBase, this.accessToken);
    }
    
    @Test
    public void getStatusById() {
        String id = "112593511260942463";
        String uriResult = "https://mastodon.social/users/kreuger458/statuses/112593511260942463";
        
        MastodonStatus status = this.statusApi.getStatusById(id);
        assertEquals(id, status.getId());
        assertEquals(uriResult, status.getUri());
    }
    
    @Test
    public void getStatusByInvalidId() {
        String id = "1125AXR11TRE9WQW63";
        
        assertThrows(ApiClientException.class, () -> {
            this.statusApi.getStatusById(id);
        });
    }
    
    @Test @Order(1)
    public void postStatus() {
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático";
        
        MastodonStatus status = this.statusApi.postStatus(text);
        idElim = status.getId();
        assertTrue(!status.getId().isEmpty());
        assertTrue(status.getContent().contains(text));
    }
    
    @Test
    public void postInvalidStatus() {
        assertThrows(MastondonApiException.class, () -> {
            this.statusApi.postStatus("");
        });
    }
    
    @Test @Order(2)
    public void deleteStatus() {
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático";
        
        MastodonStatus status = this.statusApi.deleteStatus(idElim);
        assertTrue(!status.getId().isEmpty());
        assertTrue(status.getText().contains(text));
    }
    
    @Test
    public void deleteInvalidStatus() {
        String id = "1125AXR11TRE9WQW63";
        
        assertThrows(ApiClientException.class, () -> {
            this.statusApi.deleteStatus(id);
        });
    }
    
    @Test
    public void uploadImage() throws Exception {
        String imagen = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        
        MastodonMediaAttachment mediaAttachment = this.statusApi.uploadImage(imagen);
        
        assertTrue(!mediaAttachment.getUrl().isBlank());
    }
    
    @Test @Order(7)
    public void postImage() throws Exception {
        String imagen = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático";
        
        MastodonStatus status = this.statusApi.postStatus(text, imagen);
        idElim = status.getId();
        assertTrue(!status.getId().isEmpty());
        assertTrue(status.getContent().contains(text));
    }
    
    @Test @Order(8)
    public void deletePostImage() {
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático";
        
        MastodonStatus status = this.statusApi.deleteStatus(idElim);
        assertTrue(!status.getId().isEmpty());
        assertTrue(status.getText().contains(text));
    }
    
    @Test
    public void getRebloggedBy() { // Usa el default limit
        String id = "109412553445428617";
        
        try {
            List<MastodonAccount> accounts = this.statusApi.getRebloggedBy(id);
            assertTrue(accounts.size() > 50);
        } catch (Exception e) {
            fail("Ocurrió una excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void getRebloggedByWithLimit() { // Define el  limit
        String id = "109412553445428617";
        int limit = 80;
        
        try {
            List<MastodonAccount> accounts = this.statusApi.getRebloggedBy(id, limit);
            assertTrue(accounts.size() > 50);
        } catch (Exception e) {
            fail("Ocurrió una excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void getInvalidRebloggedBy() {
        String id = "QQQ109412553445428617";
        
        assertThrows(ApiClientException.class, () -> {
            this.statusApi.getRebloggedBy(id);
        });
    }
    
    @Test
    public void getFavouritedBy() { // Usa el default limit
        String id = "109412553445428617";
        
        try {
            List<MastodonAccount> accounts = this.statusApi.getFavouritedBy(id);
            assertTrue(accounts.size() > 50);
        } catch (Exception e) {
            fail("Ocurrió una excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void getFavouritedByWithLimit() { // Define un limit
        String id = "109412553445428617";
        int limit = 80;
        
        try {
            List<MastodonAccount> accounts = this.statusApi.getFavouritedBy(id, limit);
            assertTrue(accounts.size() > 50);
        } catch (Exception e) {
            fail("Ocurrió una excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void getInvalidFavouritedBy() {
        String id = "QQQ109412553445428617";
        
        assertThrows(ApiClientException.class, () -> {
            this.statusApi.getFavouritedBy(id);
        });
    }
    
    @Test @Order(3)
    public void reblogStatus() {
        String id = "112836040801154212";
        
        MastodonStatus status = this.statusApi.reblogStatus(id);
        assertEquals(id, status.getReblog().getId());
        operationCount = status.getReblog().getReblogs_count();
        assertTrue(operationCount > 0);
    }
    
    @Test
    public void reblogInvalidStatus() {
        String id = "QQ112836040801154212";
        
        assertThrows(ApiClientException.class, () -> {
            this.statusApi.reblogStatus(id);
        });
    }
    
    @Test @Order(4)
    public void unreblogStatus() {
        String id = "112836040801154212";
        
        MastodonStatus status = this.statusApi.unreblogStatus(id);
        assertEquals(id, status.getId());
        assertEquals(operationCount - 1, status.getReblogs_count());
    }
    
    @Test
    public void unreblogInvalidStatus() {
        String id = "QQ112836040801154212";
        
        assertThrows(ApiClientException.class, () -> {
            this.statusApi.unreblogStatus(id);
        });
    }
    
    @Test @Order(5)
    public void favouriteStatus() {
        String id = "112836040801154212";
        
        MastodonStatus status = this.statusApi.favouriteStatus(id);
        assertEquals(id, status.getId());
        operationCount = status.getFavourites_count();
        assertTrue(operationCount > 0);
    }
    
    @Test
    public void favouriteInvalidStatus() {
        String id = "QQ112836040801154212";
        
        assertThrows(ApiClientException.class, () -> {
            this.statusApi.favouriteStatus(id);
        });
    }
    
    @Test @Order(6)
    public void unfavouriteStatus() {
        String id = "112836040801154212";
        
        MastodonStatus status = this.statusApi.unfavouriteStatus(id);
        assertEquals(id, status.getId());
        assertEquals(operationCount - 1, status.getFavourites_count());
    }
    
    @Test
    public void unfavouriteInvalidStatus() {
        String id = "QQ112836040801154212";
        
        assertThrows(ApiClientException.class, () -> {
            this.statusApi.unfavouriteStatus(id);
        });
    }
}