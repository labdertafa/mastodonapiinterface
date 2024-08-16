package com.laboratorio.mastodonapiinterface;

import com.laboratorio.mastodonapiinterface.model.MastodonTag;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 25/07/2024
 * @updated 16/08/2024
 */
public interface MastodonTrendingApi {
    List<MastodonTag> getTrendingTags();
    List<MastodonTag> getTrendingTags(int limit);
}