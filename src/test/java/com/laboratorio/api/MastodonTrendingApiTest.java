package com.laboratorio.api;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.mastodonapiinterface.MastodonTrendingApi;
import com.laboratorio.mastodonapiinterface.impl.MastodonTrendingApiImpl;
import com.laboratorio.mastodonapiinterface.model.MastodonTag;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 25/07/2024
 * @updated 04/05/2025
 */
public class MastodonTrendingApiTest {
    private String accessToken;
    private MastodonTrendingApi trendingApi;
    
    @BeforeEach
    private void initTests() {
        ReaderConfig config = new ReaderConfig("config//mastodon_api.properties");
        this.accessToken = config.getProperty("access_token");
        String urlBase = config.getProperty("instancia_test");
        this.trendingApi = new MastodonTrendingApiImpl(urlBase, this.accessToken);
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