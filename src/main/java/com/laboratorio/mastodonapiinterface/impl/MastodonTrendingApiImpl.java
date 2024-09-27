package com.laboratorio.mastodonapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.mastodonapiinterface.MastodonTrendingApi;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.model.MastodonTag;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 24/07/2024
 * @updated 27/09/2024
 */
public class MastodonTrendingApiImpl extends MastodonBaseApi implements MastodonTrendingApi {
    public MastodonTrendingApiImpl(String urlBase, String accessToken) {
        super(urlBase, accessToken);
    }
    
    @Override
    public List<MastodonTag> getTrendingTags() {
        return this.getTrendingTags(0);
    }

    @Override
    public List<MastodonTag> getTrendingTags(int limit) {
        String endpoint = this.apiConfig.getProperty("getTrendingTags_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getTrendingTags_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getTrendingTags_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getTrendingTags_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        
        try {
            String uri = this.urlBase + endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiPathParam("limit", Integer.toString(usedLimit));
            
            request.addApiHeader("Content-Type", "application/json");
            
            String jsonStr = this.client.executeGetRequest(request);
            
            return this.gson.fromJson(jsonStr, new TypeToken<List<MastodonTag>>(){}.getType());
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MastondonApiException(MastodonTrendingApiImpl.class.getName(), e.getMessage());
        }
    }   
}