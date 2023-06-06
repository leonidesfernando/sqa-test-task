package task.test.sqa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostComment implements Post {
    private Integer postId;
    private Integer id;
    private String name;
    private String email;
    private String body;
}
