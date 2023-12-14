package cz.abclinuxu.datoveschranky.common;

import cz.abclinuxu.datoveschranky.common.entities.Attachment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author xrosecky
 */
public class MimeTypesTest {

    @Test
    public void testfileNameToMimeType() {
        assertEquals(MimeTypes.fileNameToMimeType("sample.pdf"), "application/pdf");
        assertEquals(MimeTypes.fileNameToMimeType("a.b.c.mpeg2"), "video/mpeg");
        // bad input
        assertNull(MimeTypes.fileNameToMimeType("bez pripony"));
        assertNull(MimeTypes.fileNameToMimeType("."));
        assertNull(MimeTypes.fileNameToMimeType("e.e"));
        assertNull(MimeTypes.fileNameToMimeType("e."));
        assertNull(MimeTypes.fileNameToMimeType(".e"));
    }

    @Test
    public void testAtachment() {
        Attachment attachment = new Attachment("priklad.pdf", null);
        assertEquals(attachment.getMimeType(), "application/pdf");
        attachment.setMimeType("foo/foo");
        assertEquals(attachment.getMimeType(), "foo/foo");
        attachment.setDescription("obrazek.jpg");
        assertEquals(attachment.getMimeType(), "image/jpeg");
    }

}
