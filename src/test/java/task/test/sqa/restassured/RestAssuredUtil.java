package task.test.sqa.restassured;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RestAssuredUtil {


    public Response get(String endpoint){
        return given()
                .get(endpoint);
    }

    public Response get(Pair<String, String> param, String endPoint){
        return pathParam(param)
                .get(endPoint);
    }

    public Response getQueryParam(Pair<String, String> param,String endpoint){
        return given()
                .queryParam(param.getLeft(), param.getRight())
                .get(endpoint);
    }

    public Response post(String body, String endpoint){
        return given()
                .body(body)
                .post(endpoint);
    }

    public Response put(String body, Pair<String, String> idParam, String endpoint){
        return bodyAndPathParam(body, idParam)
                .put(endpoint);
    }

    public Response patch(String body, Pair<String, String> idParam, String endpoint){
        return bodyAndPathParam(body, idParam)
                .patch(endpoint);
    }

    public Response delete(Pair<String, String> idParam, String endpoint){
        return pathParam(idParam)
                .delete(endpoint);
    }

    private RequestSpecification bodyAndPathParam(String body, Pair<String, String> idParam){
        return given()
                .body(body)
                .pathParam(idParam.getLeft(), idParam.getRight());
    }

    private RequestSpecification pathParam(Pair<String, String> idParam){
        return given()
                .pathParam(idParam.getLeft(), idParam.getRight());
    }
}
