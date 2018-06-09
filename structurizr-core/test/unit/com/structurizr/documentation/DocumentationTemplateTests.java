package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

public class DocumentationTemplateTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;
    private StructurizrDocumentationTemplate template;
    private Documentation documentation;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        template = new StructurizrDocumentationTemplate(workspace);
        documentation = workspace.getDocumentation();
    }

    @Test
    public void test_addSection_ThrowsAnException_WhenThatSectionAlreadyExists() {
        template.addContextSection(softwareSystem, Format.Markdown, "Some Markdown content");
        assertEquals(1, documentation.getSections().size());

        try {
            template.addContextSection(softwareSystem, Format.Markdown, "Some Markdown content");
            fail();
        } catch (IllegalArgumentException iae) {
            // this is the expected exception
            assertEquals("A section of type Context for Name already exists.", iae.getMessage());
            assertEquals(1, documentation.getSections().size());
        }
    }

    @Test
    public void test_addImages_DoesNothing_WhenThereAreNoImageFilesInTheSpecifiedDirectory() throws IOException {
        template.addImages(new File(".//test/unit/com/structurizr/documentation/noimages"));
        assertTrue(documentation.getImages().isEmpty());
    }

    @Test
    public void test_addImages_ThrowsAnException_WhenTheSpecifiedDirectoryIsNull() throws IOException {
        try {
            template.addImages(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Directory path must not be null.", iae.getMessage());
        }
    }

    @Test
    public void test_addImages_ThrowsAnException_WhenTheSpecifiedDirectoryIsNotADirectory() throws IOException {
        try {
            template.addImages(new File(".//test/unit/com/structurizr/documentation/structurizr/context.md"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("context.md is not a directory."));
        }
    }

    @Test
    public void test_addImages_ThrowsAnException_WhenTheSpecifiedDirectoryDoesNotExist() throws IOException {
        try {
            template.addImages(new File(".//test/unit/com/structurizr/documentation/blah"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("blah does not exist."));
        }
    }

    @Test
    public void test_addImages_AddsAllImagesFromTheSpecifiedDirectory_WhenThereAreImageFilesInTheSpecifiedDirectory() throws IOException {
        assertTrue(documentation.getImages().isEmpty());
        Collection<Image> images = template.addImages(new File(".//test/unit/com/structurizr/documentation/images"));
        assertEquals(4, documentation.getImages().size());
        assertEquals(4, images.size());

        Image png = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", png.getType());
        assertTrue(png.getContent().startsWith("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAAD"));
        assertTrue(images.contains(png));

        Image jpg = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpg")).findFirst().get();
        assertEquals("image/jpeg", jpg.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpg.getContent());
        assertTrue(images.contains(jpg));

        Image jpeg = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpeg")).findFirst().get();
        assertEquals("image/jpeg", jpeg.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpeg.getContent());
        assertTrue(images.contains(jpeg));

        Image gif = documentation.getImages().stream().filter(i -> i.getName().equals("image.gif")).findFirst().get();
        assertEquals("image/gif", gif.getType());
        assertEquals("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", gif.getContent());
        assertTrue(images.contains(gif));
    }

    @Test
    public void test_addImages_AddsAllImagesFromTheSpecifiedDirectory_WhenThereAreImageFilesInTheSpecifiedDirectoryAndSubDirectories() throws IOException {
        assertTrue(documentation.getImages().isEmpty());
        template.addImages(new File(".//test/unit/com/structurizr/documentation"));
        assertEquals(8, documentation.getImages().size());

        Image pngInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", pngInDirectory.getType());
        assertTrue(pngInDirectory.getContent().startsWith("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAAD"));

        Image jpgInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpg")).findFirst().get();
        assertEquals("image/jpeg", jpgInDirectory.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpgInDirectory.getContent());

        Image jpegInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpeg")).findFirst().get();
        assertEquals("image/jpeg", jpegInDirectory.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpegInDirectory.getContent());

        Image gifInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.gif")).findFirst().get();
        assertEquals("image/gif", gifInDirectory.getType());
        assertEquals("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", gifInDirectory.getContent());


        Image pngInSubDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", pngInSubDirectory.getType());
        assertTrue(pngInSubDirectory.getContent().startsWith("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAAD"));

        Image jpgInSubDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpg")).findFirst().get();
        assertEquals("image/jpeg", jpgInSubDirectory.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpgInSubDirectory.getContent());

        Image jpegInSubDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpeg")).findFirst().get();
        assertEquals("image/jpeg", jpegInSubDirectory.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpegInSubDirectory.getContent());

        Image gifInSubDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.gif")).findFirst().get();
        assertEquals("image/gif", gifInSubDirectory.getType());
        assertEquals("R0lGODlhIAAaAPcAAAAAAAACCwAFHAAGFAAGIwAIFgAKHAAKJgAMKgAOMwAPPAARHwAUOQ0UHQIVMgMVJQMVLAoVKwwVJBEVHgAYOAQYJwkYORAYJQIZKwoZJQMaMgoaKwobMxQcKQsjRwMnVxcoPBsoOAAtWx8vQAAwXwIzaQ9HehZLhEVMWRRNjB5Ng0VNYUtQWkFRXhZShBVTjRRVkxlVjhtVlBtWmQpXoxFXmxZYmRFZpBhZjxpZlRValRlamAdcrgxcqg1cpCFdmw1esRReqhleqx9eoyhenhNgrAxitBlirxNktCZknw5luxllsyJlrBJmuhhmuCNnsCVnpBRptRRpvBxpryNprhVqwhtrvBxrtSJrtRxtwCVuuyFytCZ0xid0uit0wCV4xi97xDh9xDGAyzuAy0KBy0SCw0iDw0aG0EuGyjuI2EuM1EiN2EOO2EaP1USR26SipZ+kqKSmsqamrGGq76SquG+w9Gy0/Im05nK183m1+Xa2+YS27YW28YS3+Iq38Iu37HO59Iq57HS6/oq683277IS79IW77ZG77YS8+3u9/Iu++Y7A9X7B/4PB8oLC/ozC/PX3///39f///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACwAAAAAIAAaAEcI/wAlxUEhYQMGCAghaMDw4KCDCBAwVEgIYYNCDRxWxIGU0AFCDAhOMFnSREkVIEB69PDh48YNlTyMKGly5QSBCw4cYDgI4QECGFaaINkCqFCgQkiNDkKEaNAeRWaCwCDQQE6AAhY1IKTg4AABAgcOCPhKloCAs2QdGAggZ+dChg8yQNyw4cEDCRIiRMhQAYOGvxYrPNhwAAKHnQnjHnhxRQqSK4mQGkpaaJBlp4jOBJFxQILWhoMlIJBxJCgSMHWYIkK6FFEiRoLYBNGSojOLASNAa+XwFrHHiBhy5oQQwe6GEANaIOSQUIMDBSVO5MgBQ8aMGTJkxMg+w4YNGDhckP9g0BMxwsEHYlxBIsWIGDuMVDNVNAhpIjc9rMw4EGFi4gw/GVEFEl7o8cgdhkxGmWSIoCEEDAg8MBxihMEghRJSXCEIInwkZUggRxUyGSJhIAHDARlA4NFgGKQoQhJUXHGFFUgEYeONQQghRBBLWPEEER9AUFUAA1j0gIq/AafiRzwlttMDAgxAR4pHPpkQQ31RtJNwHvWXgXFKfrTABg4gABYCaKYZFpoJJFAmBAQgoFVxCGnlnAMlDIEFe1b02ScWgGJhRRZ9RjEFFCLE1dFHCLywpxRN8MBFGmyw0UYbamS6hhtfCOhECgRIYF5PGSx2RRRR9DCHI5FR5scfgUy/pgcWS8hAgEFNKqZDFFIA0QUehfyRIGWWjUjGZhH6RxwEANYgxYBa5LHHIcOKmBQhjRSCRg0/3GrBik/+FJQRv1ZGGYhMDbJIIWUUYUNnCBwmGHpASSGFqo/04SGIhazmiCB77nDARFrBJQEBIi2BBBI0YBFGGRBHTAYZY2jRwxJMqCBABysIAAKTwYlgwgsvwADDDtmZXF12271wgggeGMBCJG+gcJGdGGyQwc4ZNIAXzxlIMEEDO9OFAhySBAQAOw==", gifInSubDirectory.getContent());
    }

    @Test
    public void test_addImage_AddsTheSpecifiedImage_WhenTheSpecifiedFileExists() throws IOException {
        assertTrue(documentation.getImages().isEmpty());
        template.addImage(new File(".//test/unit/com/structurizr/documentation/image.png"));
        assertEquals(1, documentation.getImages().size());

        Image png = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", png.getType());
        assertTrue(png.getContent().startsWith("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAAD"));
    }

    @Test
    public void test_addImage_ThrowsAnException_WhenTheSpecifiedFileIsNull() throws IOException {
        try {
            template.addImage(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A file must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addImage_ThrowsAnException_WhenTheSpecifiedFileIsNotAFile() throws IOException {
        try {
            template.addImage(new File(".//test/unit/com/structurizr/documentation/"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("documentation is not a file."));
        }
    }

    @Test
    public void test_addImage_ThrowsAnException_WhenTheSpecifiedFileDoesNotExist() throws IOException {
        try {
            template.addImage(new File(".//test/unit/com/structurizr/documentation/some-other-image.png"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("some-other-image.png does not exist."));
        }
    }

    @Test
    public void test_addImage_ThrowsAnException_WhenTheSpecifiedFileIsNotAnImage() throws IOException {
        try {
            template.addImage(new File(".//test/unit/com/structurizr/documentation/structurizr/context.md"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("context.md is not a supported image file."));
        }
    }

    @Test
    public void test_readFiles_ThrowsAnException_WhenPassedAFileThatDoesNotExist() throws IOException {
        try {
            template.addContextSection(softwareSystem, new File("./no-such-file.txt"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("no-such-file.txt does not exist."));
        }
    }

    @Test
    public void test_readFiles_ThrowsAnException_WhenPassedANullFile() throws IOException {
        try {
            template.addContextSection(softwareSystem, (File)null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("One or more files must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_readFiles_ThrowsAnException_WhenPassedAnEmptyCollection() throws IOException {
        try {
            template.addContextSection(softwareSystem, new File[]{});
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("One or more files must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_readFiles_AddsAllFiles_WhenPassedADirectory() throws IOException {
        Section section = template.addContextSection(softwareSystem, new File(".//test/unit/com/structurizr/documentation/markdown"));
        assertEquals("File 1" + System.lineSeparator() +
                "File 2", section.getContent());
    }

}