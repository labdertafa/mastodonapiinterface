package com.laboratorio.mastodonapiinterface.model.response;

import com.laboratorio.mastodonapiinterface.model.MastodonStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 13/10/2024
 * @updated 13/10/2024
 */
@Getter @Setter @AllArgsConstructor
public class MastondonStatusListResponse {
    private List<MastodonStatus> statuses;
    private String nextPage;
}