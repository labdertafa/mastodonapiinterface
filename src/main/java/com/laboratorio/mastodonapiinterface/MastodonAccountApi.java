package com.laboratorio.mastodonapiinterface;

import com.laboratorio.mastodonapiinterface.model.MastodonAccount;
import com.laboratorio.mastodonapiinterface.model.MastodonRelationship;
import com.laboratorio.mastodonapiinterface.model.response.MastodonAccountListResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 10/07/2024
 * @updated 26/09/2024
 */
public interface MastodonAccountApi {
    // Obtiene la información de un usuario a partir de su ID
    MastodonAccount getAccountById(String id);
    // Obtiene la información de un usuario a partir de su username
    MastodonAccount getAccountByUsername(String username);
    // Obtiene los seguidores de un usuario a partir de su id. Quantity indica el número de registros a recuperar (cero significa todos). Se puede indicar una posición inicial.
    MastodonAccountListResponse getFollowers(String id) throws Exception;
    MastodonAccountListResponse getFollowers(String id, int limit) throws Exception;
    MastodonAccountListResponse getFollowers(String id, int limit, int quantity) throws Exception;
    MastodonAccountListResponse getFollowers(String id, int limit, int quantity, String posicionInicial) throws Exception;
    // Obtiene los seguidos de un usuario a partir de su id. Quantity indica el número de registros a recuperar (cero significa todos). Se puede indicar una posición inicial.
    MastodonAccountListResponse getFollowings(String id) throws Exception;
    MastodonAccountListResponse getFollowings(String id, int limit) throws Exception;
    MastodonAccountListResponse getFollowings(String id, int limit, int quantity) throws Exception;
    MastodonAccountListResponse getFollowings(String id, int limit, int quantity, String posicionInicial) throws Exception;
    // Seguir a un usuario
    boolean followAccount(String id);
    // Dejar de seguir a un usuario
    boolean unfollowAccount(String id);
    // Chequea la relación con un listado de cuentas identificadas por su id
    List<MastodonRelationship> checkrelationships(List<String> ids);
    // Consultar las sugerencias de seguimiento
    List<MastodonAccount> getSuggestions();
    List<MastodonAccount> getSuggestions(int limit);
    
    boolean deleteSuggestion(String userId);
}