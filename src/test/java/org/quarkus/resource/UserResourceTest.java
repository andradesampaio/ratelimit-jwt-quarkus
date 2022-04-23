//package org.quarkus.resource;
//
//import io.quarkus.test.junit.QuarkusTest;
//import org.junit.jupiter.api.Test;
//
//import static io.restassured.RestAssured.given;
//
//@QuarkusTest
//public class UserResourceTest {
//    private static final String ADMIN_USERNAME = "draande";
//    private static final String GUEST_USERNAME = "guest";
//    private static final String USER_USERNAME = "asampaio";
//    private static final String ADMIN_ROLE_NAME = "ADMIN";
//    private static final String GUEST_ROLE_NAME = "USER";
//    private static final String PASSWORD = "12345";
//
//    @Test
//    public void testAllUsersEndpointNonAuthorized() {
//
//        given()
//                .header("Authorization", "token")
//                .when().get("/api/allUsers")
//                .then()
//                .statusCode(401);
//    }
//
//    @Test
//    public void testAllUsersEndpointAuthorized(){
//        var token = getAccessToken(USER_USERNAME, PASSWORD);
//        given()
//                .header("Authorization", token)
//                .when().get("/api/allUsers")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    public void testAddUserUserRole(){
//        var token = getAccessToken(GUEST_USERNAME, PASSWORD);
//        given()
//                .header("Authorization", token)
//                .contentType(ContentType.JSON)
//                .body(getTestUser("Test New", "12345"))
//                .when().post("/api/secured/addUser")
//                .then()
//                .statusCode(401);
//    }
//
//    private String getAccessToken(String userName, String password) {
//
//        ResponseBody responseBody = given()
//                .contentType(ContentType.JSON)
//                .body(getTestUser(userName, password))
//                .when().post("/api/signIn")
//                .body();
//
//        return "Bearer " + responseBody.prettyPrint();
//    }
//
//    private JSONObject getTestUser(String userName, String password){
//        JSONObject user = new JSONObject();
//        user.put("username", userName);
//        user.put("password", password);
//        return user;
//    }
//}
