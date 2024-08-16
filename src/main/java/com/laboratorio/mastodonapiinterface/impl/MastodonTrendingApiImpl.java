package com.laboratorio.mastodonapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.mastodonapiinterface.MastodonTrendingApi;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.model.MastodonTag;
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
 * @created 24/07/2024
 * @updated 16/08/2024
 */
public class MastodonTrendingApiImpl extends MastodonBaseApi implements MastodonTrendingApi {
    public MastodonTrendingApiImpl(String accessToken) {
        super(accessToken);
    }
    
    @Override
    public List<MastodonTag> getTrendingTags() {
        return this.getTrendingTags(0);
    }

    @Override
    public List<MastodonTag> getTrendingTags(int limit) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("getTrendingTags_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getTrendingTags_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getTrendingTags_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getTrendingTags_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url)
                    .queryParam("limit", usedLimit);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new MastondonApiException(MastodonAccountApiImpl.class.getName(), str);
            }
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, new TypeToken<List<MastodonTag>>(){}.getType());
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