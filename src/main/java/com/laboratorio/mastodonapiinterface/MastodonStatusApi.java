package com.laboratorio.mastodonapiinterface;

import com.laboratorio.mastodonapiinterface.model.MastodonAccount;
import com.laboratorio.mastodonapiinterface.model.MastodonMediaAttachment;
import com.laboratorio.mastodonapiinterface.model.MastodonStatus;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 24/07/2024
 * @updated 05/06/2025
 */
public interface MastodonStatusApi {
    // Consultar un status por su id
    MastodonStatus getStatusById(String id);
    
    // Postear o eliminar un status.
    MastodonStatus postStatus(String text);
    MastodonStatus deleteStatus(String id);
    MastodonStatus postStatusWithImage(String text, MastodonMediaAttachment mediaAttachment);
    MastodonStatus postStatus(String text, String filePath);
    
    // Subir una imagen
    MastodonMediaAttachment uploadImage(String filePath);
    
    // Ver las cuentas que han impulsado o marcado como favorito un status
    List<MastodonAccount> getRebloggedBy(String id) throws Exception;
    List<MastodonAccount> getRebloggedBy(String id, int limit) throws Exception;
    List<MastodonAccount> getRebloggedBy(String id, int limit, int quantity) throws Exception;
    List<MastodonAccount> getFavouritedBy(String id) throws Exception;
    List<MastodonAccount> getFavouritedBy(String id, int limit) throws Exception;
    List<MastodonAccount> getFavouritedBy(String id, int limit, int quantity) throws Exception;
    
    // Impulsar o dejar de impulsar un status
    MastodonStatus reblogStatus(String id);
    MastodonStatus unreblogStatus(String id);
    
    // Marcar y desmarcar un status como favorito
    MastodonStatus favouriteStatus(String id);
    MastodonStatus unfavouriteStatus(String id);
    
    List<MastodonStatus> getGlobalTimeline(int quantity);
}