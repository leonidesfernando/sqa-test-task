package task.test.sqa.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import task.test.sqa.config.Configurations;
import task.test.sqa.domain.Post;
import task.test.sqa.domain.PostComment;
import task.test.sqa.domain.PostComplete;
import task.test.sqa.restassured.RestAssuredUtil;
import task.test.sqa.util.DataGen;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonPlaceHolderTests {
    private static final Configurations config = ConfigFactory.create(Configurations.class);

    @BeforeAll
    static protected void setUp(){
        RestAssured.baseURI = config.url();
    }

    @Test
    void testGetPosts(){
        Response response = RestAssuredUtil.get("posts");
        List<PostComplete> posts = response.jsonPath().getList("", PostComplete.class);
        assertEquals(200, response.statusCode());
        assertNotNull(posts);
        assertEquals(100, posts.size());
    }

    @Test
    void testGetPostById(){
        int id = DataGen.getId();
        Pair<String, String> param = Pair.of("id",  String.format("%1$s", id));
        Response response = RestAssuredUtil.get(param, "posts/{id}");
        PostComplete postComplete = response.jsonPath().getObject("", PostComplete.class);
        assertEquals(200, response.statusCode());
        assertEquals(id, postComplete.getId());
    }


    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    void testGetPostByInvalidIds(int id){
        Pair<String, String> param = Pair.of("id", String.format("%1$s", id));
        Response response = RestAssuredUtil.get(param, "posts/{id}");
        assertEquals(404, response.statusCode());
    }

    @Test
    void testGetPostComments(){
        int id = DataGen.getId();
        Pair<String, String> param = Pair.of("id",  String.format("%1$s", id));
        Response response = RestAssuredUtil.get(param, "posts/{id}/comments");
        List<PostComment> comments = response.jsonPath().getList("", PostComment.class);
        assertEquals(200, response.statusCode());
        comments.forEach( comment -> assertEquals(id, comment.getPostId()));
    }

    @Test
    void testGetPostByIdWithQueryParam(){
        int id = DataGen.getId();
        Pair<String, String> param = Pair.of("postId",  String.format("%1$s", id));
        Response response = RestAssuredUtil.getQueryParam(param, "comments");
        List<PostComment> comments = response.jsonPath().getList("", PostComment.class);
        assertEquals(200, response.statusCode());
        comments.forEach( comment -> assertEquals(id, comment.getPostId()));
    }

    @Test
    void testPost() throws JsonProcessingException {
        String body = postAsJson(DataGen.getNewPost());
        Response response = RestAssuredUtil.post(body, "posts");
        assertEquals(201, response.statusCode());
    }

    /*
        According to my understanding of the guide (https://jsonplaceholder.typicode.com/guide/),
        we shouldn't get success(200), with these bodies.
        I think it could be returned an error such as 400 Bad Request or 406 Not Acceptable, for example.
     */
    @ParameterizedTest
    @MethodSource("invalidBodies")
    void testPostWithEmptyBodies(String body) throws JsonProcessingException {
        Response response = RestAssuredUtil.post(body, "posts");
        assertEquals(201, response.statusCode());
    }

    @Test
    void testPut(){
        int id = DataGen.getId();
        Pair<String, String> param = Pair.of("id", String.format("%1$s", id));
        String body = postAsJson(DataGen.getPostComplete(id));
        Response response = RestAssuredUtil.put(body, param,"posts/{id}");
        assertEquals(200, response.statusCode());
    }

    @ParameterizedTest
    @MethodSource("postsToPatch")
    void testPatch(Post post){
        int id = DataGen.getId();
        Pair<String, String> param = Pair.of("id", String.format("%1$s", id));
        String body = postAsJson(post);
        Response response = RestAssuredUtil.patch(body, param,"posts/{id}");
        PostComplete postComplete = response.jsonPath().getObject("", PostComplete.class);
        assertEquals(200, response.statusCode());
        assertEquals(id, postComplete.getId());
    }

    /*
        According to my understanding of the guide (https://jsonplaceholder.typicode.com/guide/),
        we shouldn't get success(200), with these bodies.
        I think it could be returned an error such as 400 Bad Request or 406 Not Acceptable, for example.
     */
    @ParameterizedTest
    @MethodSource("invalidPostsToPatch")
    void testPatchWithInvalidPost(Post post, int id){
        Pair<String, String> param = Pair.of("id", String.format("%1$s", id));
        String body = postAsJson(post);
        Response response = RestAssuredUtil.patch(body, param,"posts/{id}");
        assertEquals(200, response.statusCode());
    }

    @Test
    void testDelete(){
        int id = DataGen.getId();
        Pair<String, String> param = Pair.of("id", String.format("%1$s", id));
        Response response = RestAssuredUtil.delete(param, "posts/{id}");
        assertEquals(200, response.statusCode());
    }

    private static Stream<Arguments> postsToPatch(){
        return Stream.of(
                Arguments.of(DataGen.getTitle()),
                Arguments.of(DataGen.getBody()),
                Arguments.of(DataGen.getTitleAndBody()));
    }

    private static Stream<Arguments> invalidPostsToPatch(){
        return Stream.of(
                Arguments.of(DataGen.getPostComplete(20), 20),
                Arguments.of(DataGen.getPostComplete(-1), -1)
        );
    }
    private static Stream<Arguments> invalidBodies(){
        return Stream.of(
                Arguments.of("{\"userId\":null,\"title\":null,\"body\":null}"),
                Arguments.of("{}")
        );
    }

    private String postAsJson(Post postComplete)  {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(postComplete);
        }catch (JsonProcessingException e){
            throw new IllegalStateException(e);
        }
    }
}
