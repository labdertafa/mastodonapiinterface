package com.laboratorio.mastodonapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.mastodonapiinterface.exception.MastondonApiException;
import com.laboratorio.mastodonapiinterface.model.MastodonAccount;
import com.laboratorio.mastodonapiinterface.model.response.MastodonAccountListResponse;
import com.laboratorio.mastodonapiinterface.utils.InstruccionInfo;
import com.laboratorio.mastodonapiinterface.utils.MastodonApiConfig;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 24/07/2024
 * @updated 16/08/2024
 */
public class MastodonBaseApi {
    protected static final Logger log = LogManager.getLogger(MastodonBaseApi.class);
    protected final String accessToken;
    protected MastodonApiConfig apiConfig;

    public MastodonBaseApi(String accessToken) {
        this.accessToken = accessToken;
        this.apiConfig = MastodonApiConfig.getInstance();
    }
    
    protected void logException(Exception e) {
        log.error("Error: " + e.getMessage());
        if (e.getCause() != null) {
            log.error("Causa: " + e.getMessage());
        }
    }
    
    // Función que extrae el max_id de la respuesta
    protected String extractMaxId(String str) {
        String maxId = null;
        String regex = "max_id=(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        
        if (matcher.find()) {
            maxId = matcher.group(1); // El primer grupo de captura contiene el valor de max_id
        }
        
        return maxId;
    }
    
    // Función que devuelve una página de seguidores o seguidos de una cuenta
    private MastodonAccountListResponse getAccountPage(String endpoint, String complemento, int okStatus, String id, int limit, String posicionInicial) throws Exception {
        Client client = ClientBuilder.newClient();
        Response response = null;
        
        try {
            String url = endpoint + "/" + id + "/" + complemento;
            WebTarget target = client.target(url)
                        .queryParam("limit", limit);
            if (posicionInicial != null) {
                target = target.queryParam("max_id", posicionInicial);
            }
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new MastondonApiException(MastodonBaseApi.class.getName(), str);
            }
            
            Gson gson = new Gson();
            List<MastodonAccount> accounts = gson.fromJson(jsonStr, new TypeToken<List<MastodonAccount>>(){}.getType());
            String maxId = null;
            if (!accounts.isEmpty()) {
                log.debug("Se ejecutó la query: " + url);
                log.debug("Resultados encontrados: " + accounts.size());

                String linkHeader = response.getHeaderString("link");
                log.debug("Recibí este link: " + linkHeader);
                maxId = this.extractMaxId(linkHeader);
                log.debug("Valor del max_id: " + maxId);
            }

            // return accounts;
            return new MastodonAccountListResponse(maxId, accounts);
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
    
    protected MastodonAccountListResponse getMastodonAccountList(InstruccionInfo instruccionInfo, String id, int quantity, String posicionInicial) throws Exception {
        List<MastodonAccount> accounts = null;
        boolean continuar = true;
        String endpoint = instruccionInfo.getEndpoint();
        String complemento = instruccionInfo.getComplementoUrl();
        int limit = instruccionInfo.getLimit();
        int okStatus = instruccionInfo.getOkStatus();
        String max_id = posicionInicial;
        
        if (quantity > 0) {
            limit = Math.min(limit, quantity);
        }
        
        try {
            do {
                MastodonAccountListResponse accountListResponse = this.getAccountPage(endpoint, complemento, okStatus, id, limit, max_id);
                if (accounts == null) {
                    accounts = accountListResponse.getAccounts();
                } else {
                    accounts.addAll(accountListResponse.getAccounts());
                }
                
                max_id = accountListResponse.getMaxId();
                log.debug("getMastodonAccountList. Cantidad: " + quantity + ". Recuperados: " + accounts.size() + ". Max_id: " + max_id);
                if (quantity > 0) {
                    if ((accounts.size() >= quantity) || (max_id == null)) {
                        continuar = false;
                    }
                } else {
                    if ((max_id == null) || (accountListResponse.getAccounts().size() < limit)) {
                        continuar = false;
                    }
                }
            } while (continuar);

            if (quantity == 0) {
                return new MastodonAccountListResponse(max_id, accounts);
            }
            
            return new MastodonAccountListResponse(max_id, accounts.subList(0, Math.min(quantity, accounts.size())));
        } catch (Exception e) {
            throw e;
        }
    }
}