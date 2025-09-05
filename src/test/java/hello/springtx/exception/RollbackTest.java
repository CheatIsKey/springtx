package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
public class RollbackTest {

    @Autowired
    RollbackService rollbackService;

    @Test
    void runtimeExcepion() {
        Assertions.assertThatThrownBy(() -> rollbackService.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedExcepion() {
        Assertions.assertThatThrownBy(() -> rollbackService.checkedException())
                .isInstanceOf(MyExceptuon.class);
    }

    @Test
    void rollbackForExcepion() {
        Assertions.assertThatThrownBy(() -> rollbackService.rollbackFor())
                .isInstanceOf(MyExceptuon.class);
    }

    @TestConfiguration
    static class RollbackTestConfig {

        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    static class RollbackService {

        // 런타임 예외 발생 -> 롤백
        @Transactional
        public void runtimeException() {
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        // 체크 예외 발생 -> 커밋
        @Transactional
        public void checkedException() throws MyExceptuon {
            log.info("call checkedException");
            throw new MyExceptuon();
        }

        // 체크 예외 rollbackFor 지정 -> 롤백
        @Transactional(rollbackFor = MyExceptuon.class)
        public void rollbackFor() throws MyExceptuon {
            log.info("call rollbackFor");
            throw new MyExceptuon();
        }
    }

    static class MyExceptuon extends Exception{}



}
