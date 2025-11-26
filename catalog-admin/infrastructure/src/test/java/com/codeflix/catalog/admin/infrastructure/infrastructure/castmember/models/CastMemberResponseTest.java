package com.codeflix.catalog.admin.infrastructure.infrastructure.castmember.models;

import com.codeflix.catalog.admin.infrastructure.JacksonTest;
import com.codeflix.catalog.admin.infrastructure.castmember.models.CastMemberResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CastMemberResponseTest {

    @Autowired
    private JacksonTester<CastMemberResponse> json;

    @Test
    public void testMarshall() throws Exception {

    }

    @Test
    public void testUnmarshall() throws Exception {

    }

}
