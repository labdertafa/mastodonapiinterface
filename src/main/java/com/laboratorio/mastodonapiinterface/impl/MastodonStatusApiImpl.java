package com.laboratorio.mastodonapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.mastodonapiinterface.MastodonStatusApi;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.model.MastodonAccount;
import com.laboratorio.mastodonapiinterface.model.MastodonMediaAttachment;
import com.laboratorio.mastodonapiinterface.model.MastodonStatus;
import com.laboratorio.mastodonapiinterface.model.response.MastodonAccountListResponse;
import com.laboratorio.mastodonapiinterface.utils.InstruccionInfo;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 24/07/2024
 * @updated 16/09/2024
 */
public class MastodonStatusApiImpl extends MastodonBaseApi implements MastodonStatusApi {
    public MastodonStatusApiImpl(String accessToken) {
        super(accessToken);
    }
    
    @Override
    public MastodonStatus getStatusById(String id) {
        String endpoint = this.apiConfig.getProperty("getStatusById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getStatusById_ok_status"));
        
        try {
            String uri = endpoint + "/" + id;
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            
            String jsonStr = this.client.executeGetRequest(request);
            
            return this.gson.fromJson(jsonStr, MastodonStatus.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public MastodonStatus postStatus(String text) {
        return this.postStatus(text, null);
    }

    @Override
    public MastodonStatus deleteStatus(String id) {
        String endpoint = this.apiConfig.getProperty("deleteStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("deleteStatus_ok_status"));
        
        try {
            String uri = endpoint + "/" + id;
            
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            String jsonStr = this.client.executeDeleteRequest(request);
            
            return this.gson.fromJson(jsonStr, MastodonStatus.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }
    
    @Override
    public MastodonStatus postStatusWithImage(String text, MastodonMediaAttachment mediaAttachment) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));
        
        try {
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiPathParam("status", text);
            request.addApiPathParam("visibility", "public");
            request.addApiPathParam("language", "es");
            if (mediaAttachment != null) {
                request.addApiPathParam("media_ids[]", mediaAttachment.getId());
            }
            
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            String jsonStr = this.client.executePostRequest(request);
            
            return this.gson.fromJson(jsonStr, MastodonStatus.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }
    
    @Override
    public MastodonStatus postStatus(String text, String filePath) {
        try {
            if (filePath != null) {
                MastodonMediaAttachment mediaAttachment = this.uploadImage(filePath);
                return this.postStatusWithImage(text, mediaAttachment);
            }
            
            return this.postStatusWithImage(text, null);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }
    
    @Override
    public MastodonMediaAttachment uploadImage(String filePath) throws Exception {
        String endpoint = this.apiConfig.getProperty("UploadImage_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("UploadImage_ok_status"));
        
        try {
            String uri = endpoint;
            
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            request.addFileFormData("file", filePath);
                        
            String jsonStr = this.client.executePostRequest(request);
            
            return this.gson.fromJson(jsonStr, MastodonMediaAttachment.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }
    
    @Override
    public List<MastodonAccount> getRebloggedBy(String id) throws Exception {
        return this.getRebloggedBy(id, 0);
    }
    
    @Override
    public List<MastodonAccount> getRebloggedBy(String id, int limit) throws Exception {
        return this.getRebloggedBy(id, limit, 0);
    }
    
    @Override
    public List<MastodonAccount> getRebloggedBy(String id, int limit, int quantity) throws Exception {
        String endpoint = this.apiConfig.getProperty("getRebloggedBy_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getRebloggedBy_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getRebloggedBy_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getRebloggedBy_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getRebloggedBy_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(endpoint, complementoUrl, okStatus, usedLimit);
        MastodonAccountListResponse accountListResponse = this.getMastodonAccountList(instruccionInfo, id, quantity, null);
        return accountListResponse.getAccounts();
    }
    
    @Override
    public List<MastodonAccount> getFavouritedBy(String id) throws Exception {
        return this.getFavouritedBy(id, 0);
    }
    
    @Override
    public List<MastodonAccount> getFavouritedBy(String id, int limit) throws Exception {
        return this.getFavouritedBy(id, limit, 0);
    }
    
    @Override
    public List<MastodonAccount> getFavouritedBy(String id, int limit, int quantity) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFavouritedBy_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFavouritedBy_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFavouritedBy_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getFavouritedBy_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getFavouritedBy_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(endpoint, complementoUrl, okStatus, usedLimit);
        MastodonAccountListResponse accountListResponse = this.getMastodonAccountList(instruccionInfo, id, quantity, null);
        return accountListResponse.getAccounts();
    }
    
    private MastodonStatus executeSimplePost(String uri, int okStatus) {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus);
            
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            String jsonStr = this.client.executePostRequest(request);
            
            return this.gson.fromJson(jsonStr, MastodonStatus.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public MastodonStatus reblogStatus(String id) {
        String endpoint = this.apiConfig.getProperty("reblogStatus_endpoint");
        String complementoUrl = this.apiConfig.getProperty("reblogStatus_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("reblogStatus_ok_status"));
        String url = endpoint + "/" + id + "/" + complementoUrl;
        return executeSimplePost(url, okStatus);
    }

    @Override
    public MastodonStatus unreblogStatus(String id) {
        String endpoint = this.apiConfig.getProperty("unreblogStatus_endpoint");
        String complementoUrl = this.apiConfig.getProperty("unreblogStatus_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("unreblogStatus_ok_status"));
        String url = endpoint + "/" + id + "/" + complementoUrl;
        return executeSimplePost(url, okStatus);
    }

    @Override
    public MastodonStatus favouriteStatus(String id) {
        String endpoint = this.apiConfig.getProperty("favouriteStatus_endpoint");
        String complementoUrl = this.apiConfig.getProperty("favouriteStatus_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("favouriteStatus_ok_status"));
        String url = endpoint + "/" + id + "/" + complementoUrl;
        return executeSimplePost(url, okStatus);
    }

    @Override
    public MastodonStatus unfavouriteStatus(String id) {
        String endpoint = this.apiConfig.getProperty("unfavouriteStatus_endpoint");
        String complementoUrl = this.apiConfig.getProperty("unfavouriteStatus_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("unfavouriteStatus_ok_status"));
        String url = endpoint + "/" + id + "/" + complementoUrl;
        return executeSimplePost(url, okStatus);
    }   
}