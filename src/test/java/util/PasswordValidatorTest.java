package util;

import com.upickem.util.PasswordValidator;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PasswordValidatorTest {

    @Test
    public void testValidate() {

        //worst case
        assertThat(PasswordValidator.validate("password")).isFalse();
        //no number
        assertThat(PasswordValidator.validate("IamGoodPassword!")).isFalse();
        //no special char
        assertThat(PasswordValidator.validate("IamGoodPassword1")).isFalse();
        //not long enough
        assertThat(PasswordValidator.validate("Iam1!")).isFalse();
        //no caps
        assertThat(PasswordValidator.validate("iamgoodpassword1!")).isFalse();
        //happy path
        assertThat(PasswordValidator.validate("IamGoodPassword1!")).isTrue();
    }
}
