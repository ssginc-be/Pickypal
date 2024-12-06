package util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Queue-ri
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private int statusCode; // 정상 응답은 200, 아닌 값은 error
    private String jsonStr; // json string
}
