package com.laboratorio.mastodonapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.mastodonapiinterface.MastodonAccountApi;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.model.MastodonAccount;
import com.laboratorio.mastodonapiinterface.model.MastodonRelationship;
import com.laboratorio.mastodonapiinterface.model.response.MastodonAccountListResponse;
import com.laboratorio.mastodonapiinterface.model.response.MastodonFollowResponse;
import com.laboratorio.mastodonapiinterface.utils.InstruccionInfo;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 10/07/2024
 * @updated 16/08/2024
 */
public class MastodonAccountApiImpl extends MastodonBaseApi implements MastodonAccountApi {
    public MastodonAccountApiImpl(String accessToken) {
        super(accessToken);
    }
    
    @Override
    public MastodonAccount getAccountById(String id) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("getAccountById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountById_ok_status"));
        
        try {
            String url = endpoint + "/" + id;
            WebTarget target = client.target(url);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new MastondonApiException(MastodonAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, MastodonAccount.class);
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
    public MastodonAccount getAccountByUsername(String username) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("getAccountByUsername_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountByUsername_ok_status"));
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url)
                    .queryParam("acct", username);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new MastondonApiException(MastodonAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, MastodonAccount.class);
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
    public MastodonAccountListResponse getFollowers(String id) throws Exception {
        return this.getFollowers(id, 0);
    }
    
    @Override
    public MastodonAccountListResponse getFollowers(String id, int limit) throws Exception {
        return this.getFollowers(id, limit, 0);
    }
    
    @Override
    public MastodonAccountListResponse getFollowers(String id, int limit, int quantity) throws Exception {
        return getFollowers(id, limit, quantity, null);
    }
    
    @Override
    public MastodonAccountListResponse getFollowers(String id, int limit, int quantity, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFollowers_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowers_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowers_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowers_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowers_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(endpoint, complementoUrl, okStatus, usedLimit);
        return this.getMastodonAccountList(instruccionInfo, id, quantity, posicionInicial);
    }
    
    @Override
    public MastodonAccountListResponse getFollowings(String id) throws Exception {
        return this.getFollowings(id, 0);
    }
    
    @Override
    public MastodonAccountListResponse getFollowings(String id, int limit) throws Exception {
        return this.getFollowings(id, limit, 0);
    }
    
    @Override
    public MastodonAccountListResponse getFollowings(String id, int limit, int quantity) throws Exception {
        return this.getFollowings(id, limit, quantity, null);
    }
    
    @Override
    public MastodonAccountListResponse getFollowings(String id, int limit, int quantity, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFollowings_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowings_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowings_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowings_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowings_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(endpoint, complementoUrl, okStatus, usedLimit);
        return this.getMastodonAccountList(instruccionInfo, id, quantity, posicionInicial);
    }
    
    @Override
    public boolean followAccount(String id) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("followAccount_endpoint");
        String complementoUrl = this.apiConfig.getProperty("followAccount_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("followAccount_ok_status"));
        
        try {
            String url = endpoint + "/" + id + "/" + complementoUrl;
            WebTarget target = client.target(url);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .post(Entity.text(""));
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new MastondonApiException(MastodonAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            Gson gson = new Gson();
            MastodonFollowResponse followResponse = gson.fromJson(jsonStr, MastodonFollowResponse.class);
            return followResponse.isFollowing();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (MastondonApiException e) {
            throw  e;
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }
    
    @Override
    public boolean unfollowAccount(String id) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("unfollowAccount_endpoint");
        String complementoUrl = this.apiConfig.getProperty("unfollowAccount_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("unfollowAccount_ok_status"));
        
        try {
            String url = endpoint + "/" + id + "/" + complementoUrl;
            WebTarget target = client.target(url);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .post(Entity.text(""));
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new MastondonApiException(MastodonAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            Gson gson = new Gson();
            MastodonFollowResponse followResponse = gson.fromJson(jsonStr, MastodonFollowResponse.class);
            return !followResponse.isFollowing();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (MastondonApiException e) {
            throw  e;
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }
    
    @Override
    public List<MastodonRelationship> checkrelationships(List<String> ids) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("checkrelationships_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("checkrelationships_ok_status"));
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url);
            for (String id : ids) {
                target = target.queryParam("id[]", id);
            }
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new MastondonApiException(MastodonAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, new TypeToken<List<MastodonRelationship>>(){}.getType());
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
}