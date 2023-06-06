package task.test.sqa.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import task.test.sqa.domain.Post;
import task.test.sqa.domain.PostComplete;
import task.test.sqa.domain.Title;
import task.test.sqa.domain.TitleAndBody;
import task.test.sqa.domain.Body;
import task.test.sqa.domain.NewPost;

import java.util.Random;

@UtilityClass
public class DataGen {

    public Post getNewPost(){
        return NewPost.builder()
                .title("Any title")
                .userId(1012)
                .body("Simple body text")
                .build();
    }

    public Post getPostComplete(int id){
        return PostComplete.builder()
                .title("Any title")
                .userId(id)
                .body("Simple body text")
                .build();
    }

    public Post getTitle(){
        return Title.builder()
                .title("Simple title")
                .build();
    }

    public Post getBody(){
        return Body.builder()
                .body("Simple body")
                .build();
    }

    public Post getTitleAndBody(){
        return TitleAndBody.builder()
                .title("Simple title")
                .body("Simple body")
                .build();
    }

    public int getId(){
        Random random = new Random();
        return random.nextInt(1, 100);
    }
}
