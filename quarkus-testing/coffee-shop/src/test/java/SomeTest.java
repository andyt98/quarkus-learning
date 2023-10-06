import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SomeTest {

    @AfterEach
    void afterEach() {
        System.out.println("afterEach");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }

    @Test
    void test1() {
        System.out.println("running test 1");

        assertThat("test").isEqualToIgnoringWhitespace("te st");
        assertThat("test").isEqualToIgnoringCase("TeSt");

        List<String> strings = List.of("running", "tests");
        assertThat(strings).contains("tests");
        assertThat(strings).containsExactly("running", "tests");
    }

    @Test
    void test2() {
        System.out.println("running test 2");
    }

    @Test
    void test3() {
        System.out.println("running test 3");
    }

    @Test
    void test4() {
        System.out.println("running test 4");
    }

    @Test
    void test5() {
        System.out.println("running test 5");
    }

    @Test
    void test6() {
        System.out.println("running test 6");
    }

    @Test
    void test7() {
        System.out.println("running test 7");
    }

    @Test
    void test8() {
        System.out.println("running test 8");
    }

    @Test
    void test9() {
        System.out.println("running test 9");
    }

    @Test
    void test10() {
        System.out.println("running test 10");
    }
}

