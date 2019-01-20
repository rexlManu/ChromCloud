package me.rexlmanu.chromcloudnode.web.defaults;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class User {

    private String userName;
    private String token;
    private List<String> subNodes;
}
