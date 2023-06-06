package task.test.sqa.domain;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostComplete implements Post {
    private Integer userId;
    private Integer id;
    private String title;
    private String body;
}
