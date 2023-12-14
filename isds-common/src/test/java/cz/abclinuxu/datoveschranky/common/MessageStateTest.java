package cz.abclinuxu.datoveschranky.common;

import cz.abclinuxu.datoveschranky.common.entities.MessageState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

/**
 *
 * @author xrosecky
 */
public class MessageStateTest {


    @Test
    public void test() {
        int value1 = MessageState.toInt(EnumSet.of(MessageState.READ));
        Assertions.assertEquals(128, value1);
        int value4 = MessageState.toInt(EnumSet.of(MessageState.DELIVERED_BY_FICTION,
                MessageState.DELIVERED_BY_LOGIN));
        Assertions.assertEquals(96, value4);
    }

}
