package com.laboratorio.api;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.mastodonapiinterface.MastodonAccountApi;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.impl.MastodonAccountApiImpl;
import com.laboratorio.mastodonapiinterface.model.MastodonAccount;
import com.laboratorio.mastodonapiinterface.model.MastodonRelationship;
import com.laboratorio.mastodonapiinterface.model.response.MastodonAccountListResponse;
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
 * @created 10/07/2024
 * @updated 05/06/2025
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MastodonAccountApiTest {
    private String accessToken;
    private MastodonAccountApi accountApi;

    @BeforeEach
    private void initTest() {
        ReaderConfig config = new ReaderConfig("config//mastodon_api.properties");
        this.accessToken = config.getProperty("access_token");
        String urlBase = config.getProperty("instancia_test");
        this.accountApi = new MastodonAccountApiImpl(urlBase, this.accessToken);
    }
    
    @Test
    public void findAccountById() {
        String id = "109331490794268038";
        String resultado = "rober@masto.es";
        
        MastodonAccount account = this.accountApi.getAccountById(id);
        assertEquals(id, account.getId());
        assertEquals(resultado, account.getAcct());
    }
    
    @Test
    public void findAccountByInvalidId() {
        String id = "1125349753AAABBB60";
        
        assertThrows(MastondonApiException.class, () -> {
            this.accountApi.getAccountById(id);
        });
    }
    
    @Test
    public void findAccountByAcct() {
        String acct = "rober@masto.es";
        String resultado = "109331490794268038";
        
        MastodonAccount account = this.accountApi.getAccountByUsername(acct);
        assertEquals(acct, account.getAcct());
        assertEquals(resultado, account.getId());
    }
    
    @Test
    public void findAccountByInvalidAcct() {
        String acct = "@ZZZWWWWPPPSSSDDGGGFF";
        
        assertThrows(MastondonApiException.class, () -> {
            this.accountApi.getAccountByUsername(acct);
        });
    }
    
    @Test
    public void get40Followers() throws Exception {
        String id = "109285474114426957";
        int maxLimit = 80;
        int cantidad = 40;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowers(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }
    
    @Test
    public void get40FollowersDefaultLimit() throws Exception {
        String id = "109285474114426957";
        int defaultLimit = 0;
        int cantidad = 40;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowers(id, defaultLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }
    
    @Test
    public void get80Followers() throws Exception {
        String id = "109285474114426957";
        int maxLimit = 80;
        int cantidad = 80;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowers(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }
    
    @Test
    public void get81Followers() throws Exception {
        String id = "109285474114426957";
        int maxLimit = 80;
        int cantidad = 81;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowers(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }
    
    @Test
    public void get200Followers() throws Exception {
        String id = "109285474114426957";
        int maxLimit = 80;
        int cantidad = 200;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowers(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }

    @Test
    public void getAllFollowers() throws Exception {     // Usa default limit
        String id = "109285474114426957";
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowers(id);
        assertTrue(accountListResponse.getMaxId() == null);
        assertTrue(!accountListResponse.getAccounts().isEmpty());
    }
    
    @Test
    public void getFollowersInvalidId() {
        String id = "1125349753AAABBB60";
        
        assertThrows(MastondonApiException.class, () -> {
            this.accountApi.getFollowers(id);
        });
    }
    
    @Test
    public void get40Followings() throws Exception {
        String id = "109285474114426957";
        int maxLimit = 80;
        int cantidad = 40;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowings(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }
    
    @Test
    public void get40FollowingsDefaultLimit() throws Exception {
        String id = "109285474114426957";
        int defaulLimit = 0;
        int cantidad = 40;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowings(id, defaulLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }
    
    @Test
    public void get80Followings() throws Exception {
        String id = "109285474114426957";
        int maxLimit = 80;
        int cantidad = 80;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowings(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }
    
    @Test
    public void get81Followings() throws Exception {
        String id = "109285474114426957";
        int maxLimit = 80;
        int cantidad = 81;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowings(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }
    
    @Test
    public void get200Followings() throws Exception {
        String id = "109285474114426957";
        int maxLimit = 80;
        int cantidad = 200;
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowings(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getMaxId().isEmpty());
    }

    @Test
    public void getAllFollowings() throws Exception { // Usa default limit
        String id = "109285474114426957";
        // String id = "112727255328679336";   // Siguiendo 0 cuentas
        
        MastodonAccountListResponse accountListResponse = this.accountApi.getFollowings(id);

        assertTrue(accountListResponse.getAccounts().size() >= 0);
        assertTrue(accountListResponse.getMaxId() == null);
    }
    
    @Test
    public void getFollowingsInvalidId() {
        String id = "1125349753AAABBB60";
        
        assertThrows(MastondonApiException.class, () -> {
            this.accountApi.getFollowings(id);
        });
    }
    
    @Test @Order(1)
    public void followAccount() {
        String id = "113451993690948867";
        
        boolean result = this.accountApi.followAccount(id);
        
        assertTrue(result);
    }
    
    @Test
    public void followInvalidAccount() {
        String id = "1125349753AAABBB60";
        
        assertThrows(MastondonApiException.class, () -> {
            this.accountApi.followAccount(id);
        });
    }
    
    @Test @Order(2)
    public void unfollowAccount() {
        String id = "109333584977490945";
        
        boolean result = this.accountApi.unfollowAccount(id);
        
        assertTrue(result);
    }
    
    @Test
    public void unfollowInvalidAccount() {
        String id = "1125349753AAABBB60";
        
        assertThrows(MastondonApiException.class, () -> {
            this.accountApi.unfollowAccount(id);
        });
    }
    
    @Test
    public void checkRelationships() {
        List<String> ids = List.of("109285474114426957", "113253912705605249");
        
        List<MastodonRelationship> list = this.accountApi.checkrelationships(ids);
        assertTrue(list.size() == 2);
    }
    
    @Test
    public void checkMutualRelationship() {
        List<String> ids = List.of("113253912705605249");
        
        List<MastodonRelationship> list = this.accountApi.checkrelationships(ids);
        assertTrue(list.get(0).isFollowing());
        assertTrue(list.get(0).isFollowed_by());
    }
    
    @Test
    public void getSuggestionsWithoutLimit() {
        ReaderConfig config = new ReaderConfig("config//mastodon_api.properties");
        int defaultLimit = Integer.parseInt(config.getProperty("getSuggestions_default_limit"));
        
        List<MastodonAccount> accounts = this.accountApi.getSuggestions();
        
        assertEquals(defaultLimit, accounts.size());
    }
    
    @Test
    public void getSuggestionsWithLimit() {
        int limit = 10;
        
        List<MastodonAccount> accounts = this.accountApi.getSuggestions(limit);
        
        assertEquals(limit, accounts.size());
    }
    
/*    @Test
    public void deleteSuggestion() {
        int limit = 10;
        
        List<MastodonAccount> accounts = this.accountApi.getSuggestions(limit);
        boolean result = this.accountApi.deleteSuggestion(accounts.get(0).getId());
        
        assertTrue(result);
    } */
}