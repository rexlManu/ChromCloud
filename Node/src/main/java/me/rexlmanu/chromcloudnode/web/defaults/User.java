package me.rexlmanu.chromcloudnode.web.defaults;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class User {

    private String userName;
    private String token;
}
