package com.imooc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kent
 * @date 2017-10-24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggerTest {

    @Test
    public void test1() {
        String name = "imooc";
        String password = "123456";

        log.debug("debug:...");
        log.info("info:...{} is name",name);
        log.error("error:....");
        log.warn("warn....");
    }
}
