package com.laboratorio.mastodonapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.clientapilibrary.exceptions.ApiClientException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.mastodonapiinterface.MastodonStatusApi;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.model.MastodonAccount;
import com.laboratorio.mastodonapiinterface.model.MastodonMediaAttachment;
import com.laboratorio.mastodonapiinterface.model.MastodonStatus;
import com.laboratorio.mastodonapiinterface.model.response.MastodonAccountListResponse;
import com.laboratorio.mastodonapiinterface.model.response.MastondonStatusListResponse;
import com.laboratorio.mastodonapiinterface.utils.InstruccionInfo;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rafael
 * @version 1.4
 * @created 24/07/2024
 * @updated 18/04/2025
 */
public class MastodonStatusApiImpl extends MastodonBaseApi implements MastodonStatusApi {
    public MastodonStatusApiImpl(String urlBase, String accessToken) {
        super(urlBase, accessToken);
    }
    
    @Override
    public MastodonStatus getStatusById(String id) {
        String endpoint = this.apiConfig.getProperty("getStatusById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getStatusById_ok_status"));
        
        try {
            String uri = this.urlBase + endpoint + "/" + id;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), MastodonStatus.class);
        } catch (ApiClientException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            logException(e);
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
            String uri = this.urlBase + endpoint + "/" + id;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.DELETE);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), MastodonStatus.class);
        } catch (ApiClientException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            logException(e);
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }
    
    @Override
    public MastodonStatus postStatusWithImage(String text, MastodonMediaAttachment mediaAttachment) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));
        
        try {
            String uri = this.urlBase + endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            request.addApiPathParam("status", text);
            request.addApiPathParam("visibility", "public");
            request.addApiPathParam("language", "es");
            if (mediaAttachment != null) {
                request.addApiPathParam("media_ids[]", mediaAttachment.getId());
            }
            
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), MastodonStatus.class);
        } catch (ApiClientException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            logException(e);
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
        } catch (Exception e) {
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }
    
    @Override
    public MastodonMediaAttachment uploadImage(String filePath) throws Exception {
        String endpoint = this.apiConfig.getProperty("UploadImage_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("UploadImage_ok_status"));
        
        try {
            String uri = this.urlBase + endpoint;
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            request.addFileFormData("file", filePath);
                        
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), MastodonMediaAttachment.class);
        } catch (ApiClientException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            logException(e);
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
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            request.addApiHeader("Content-Type", "application/json");
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), MastodonStatus.class);
        } catch (ApiClientException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            logException(e);
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public MastodonStatus reblogStatus(String id) {
        String endpoint = this.apiConfig.getProperty("reblogStatus_endpoint");
        String complementoUrl = this.apiConfig.getProperty("reblogStatus_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("reblogStatus_ok_status"));
        String url = this.urlBase + endpoint + "/" + id + "/" + complementoUrl;
        return executeSimplePost(url, okStatus);
    }

    @Override
    public MastodonStatus unreblogStatus(String id) {
        String endpoint = this.apiConfig.getProperty("unreblogStatus_endpoint");
        String complementoUrl = this.apiConfig.getProperty("unreblogStatus_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("unreblogStatus_ok_status"));
        String url = this.urlBase + endpoint + "/" + id + "/" + complementoUrl;
        return executeSimplePost(url, okStatus);
    }

    @Override
    public MastodonStatus favouriteStatus(String id) {
        String endpoint = this.apiConfig.getProperty("favouriteStatus_endpoint");
        String complementoUrl = this.apiConfig.getProperty("favouriteStatus_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("favouriteStatus_ok_status"));
        String url = this.urlBase + endpoint + "/" + id + "/" + complementoUrl;
        return executeSimplePost(url, okStatus);
    }

    @Override
    public MastodonStatus unfavouriteStatus(String id) {
        String endpoint = this.apiConfig.getProperty("unfavouriteStatus_endpoint");
        String complementoUrl = this.apiConfig.getProperty("unfavouriteStatus_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("unfavouriteStatus_ok_status"));
        String url = this.urlBase + endpoint + "/" + id + "/" + complementoUrl;
        return executeSimplePost(url, okStatus);
    }
    
    private String getNextPageLink(String input) {
        // Expresión regular para buscar la URL de "rel=next"
        String regex = "<([^>]+)>;\\s*rel=\"next\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return null;
    }
    
    private MastondonStatusListResponse getTimelinePage(String uri, int okStatus, int limit, String nextPage) {
        try {
            ApiRequest request;
            if (nextPage == null) {
                request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            } else {
                request = new ApiRequest(nextPage, okStatus, ApiMethodType.GET);
            }
            request.addApiPathParam("limit", Integer.toString(limit));
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            List<MastodonStatus> statuses = this.gson.fromJson(response.getResponseStr(), new TypeToken<List<MastodonStatus>>(){}.getType());
            String newNextPage = null;
            if (!statuses.isEmpty()) {
                log.debug("Se ejecutó la query: " + uri);
                log.debug("Resultados encontrados: " + statuses.size());

                List<String> linkHeaderList = response.getHttpHeaders().get("Link");
                if ((linkHeaderList != null) && (!linkHeaderList.isEmpty())) {
                    String linkHeader = linkHeaderList.get(0);
                    log.debug("Recibí este Link: " + linkHeader);
                    newNextPage = this.getNextPageLink(linkHeader);
                    log.debug("Valor del newNextPage: " + newNextPage);
                }
            }

            return new MastondonStatusListResponse(statuses, newNextPage);
        } catch (ApiClientException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            logException(e);
            throw new MastondonApiException(MastodonStatusApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public List<MastodonStatus> getGlobalTimeline(int quantity) {
        String endpoint = this.apiConfig.getProperty("getGlobalTimeLine_endpoint");
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getGlobalTimeLine_default_limit"));
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getGlobalTimeLine_ok_status"));
        
        List<MastodonStatus> statuses = null;
        boolean continuar = true;
        String nextPage = null;
        
        try {
            String uri = this.urlBase + endpoint;
            
            do {
                MastondonStatusListResponse statusListResponse = this.getTimelinePage(uri, okStatus, defaultLimit, nextPage);
                log.debug("Elementos recuperados total: " + statusListResponse.getStatuses().size());
                if (statuses == null) {
                    statuses = statusListResponse.getStatuses();
                } else {
                    statuses.addAll(statusListResponse.getStatuses());
                }
                
                nextPage = statusListResponse.getNextPage();
                log.debug("getGlobalTimeline. Recuperados: " + statuses.size() + ". Next page: " + nextPage);
                if (statusListResponse.getStatuses().isEmpty()) {
                    continuar = false;
                } else {
                    if ((nextPage == null) || (statuses.size() >= quantity)) {
                        continuar = false;
                    }
                }
            } while (continuar);
            
            return statuses.subList(0, Math.min(quantity, statuses.size()));
        } catch (Exception e) {
            throw e;
        }
    }
}