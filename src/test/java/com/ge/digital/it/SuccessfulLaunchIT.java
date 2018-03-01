package com.ge.digital.it;

import com.ge.digital.Application;
import com.ge.digital.model.Info;
import com.jayway.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SuccessfulLaunchIT {

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void init() {
        RestAssured.port = serverPort;
    }

    @Test
    public void testGet() {
        Info result = RestAssured.when()
                .get("/api/v1/info")
                .then()
                .statusCode(HttpStatus.OK.value()).extract().body().as(Info.class);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getProjectName()).isNotEmpty();
        Assertions.assertThat(result.getVersion()).isNotNull();
    }

}
