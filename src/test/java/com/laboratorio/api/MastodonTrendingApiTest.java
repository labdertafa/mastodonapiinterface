package com.laboratorio.api;

import com.laboratorio.mastodonapiinterface.MastodonTrendingApi;
import com.laboratorio.mastodonapiinterface.impl.MastodonTrendingApiImpl;
import com.laboratorio.mastodonapiinterface.model.MastodonTag;
import com.laboratorio.mastodonapiinterface.utils.MastodonApiConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 25/07/2024
 * @updated 21/08/2024
 */
public class MastodonTrendingApiTest {
    private String accessToken;
    private MastodonTrendingApi trendingApi;
    
    @BeforeEach
    private void initTests() {
        this.accessToken = MastodonApiConfig.getInstance().getProperty("access_token");
        this.trendingApi = new MastodonTrendingApiImpl(this.accessToken);
    }
    
    @Test
    public void getTrendingTags() { // Cantidad por defecto
        int cantidad = 10;
        
        List<MastodonTag> tags = this.trendingApi.getTrendingTags();

        assertEquals(cantidad, tags.size());
    }
    
    @Test
    public void getTrendingTagsWithLimit() { // Cantidad definida
        int limit = 8;
        
        List<MastodonTag> tags = this.trendingApi.getTrendingTags(limit);

        assertEquals(limit, tags.size());
    }
}