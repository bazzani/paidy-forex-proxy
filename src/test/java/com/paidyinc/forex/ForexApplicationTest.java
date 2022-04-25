package com.paidyinc.forex;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest
class ForexApplicationTest {

    @Autowired
    private ForexController forexController;

    @Test
    void shouldLoadContextAndInjectNonNullController() {
        // given
        // when
        // then
        assertThat(forexController, is(notNullValue()));
    }

    @Test
    void shouldCallMainMethodOnApplication() {
        // given
        // when
        ForexApplication application = new ForexApplication();
        application.main(new String[]{""});

        // then
        assertThat(application, is(notNullValue()));
    }
}
