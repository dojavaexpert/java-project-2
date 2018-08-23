package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ModelItemTests extends AbstractWorkspaceTestBase {

   @Test
   public void test_construction() {
       Element element = model.addSoftwareSystem("Name", "Description");
       assertEquals("Name", element.getName());
       assertEquals("Description", element.getDescription());
   }

    @Test
    public void test_getTags_WhenThereAreNoTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    public void test_getTags_ReturnsTheListOfTags_WhenThereAreSomeTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags("tag1", "tag2", "tag3");
        assertEquals("Element,Software System,tag1,tag2,tag3", element.getTags());
    }

    @Test
    public void test_setTags_DoesNotDoAnything_WhenPassedNull() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setTags(null);
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    public void test_addTags_DoesNotDoAnything_WhenPassedNull() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags((String)null);
        assertEquals("Element,Software System", element.getTags());

        element.addTags(null, null, null);
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    public void test_addTags_AddsTags_WhenPassedSomeTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags(null, "tag1", null, "tag2");
        assertEquals("Element,Software System,tag1,tag2", element.getTags());
    }

    @Test
    public void test_addTags_AddsTags_WhenPassedSomeTagsAndThereAreDuplicateTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags(null, "tag1", null, "tag2", "tag2");
        assertEquals("Element,Software System,tag1,tag2", element.getTags());
    }

    @Test
    public void test_getProperties_ReturnsAnEmptyList_WhenNoPropertiesHaveBeenAdded() {
        Element element = model.addSoftwareSystem("Name", "Description");
        assertEquals(0, element.getProperties().size());
    }

    @Test
    public void test_addProperty_ThrowsAnException_WhenTheNameIsNull() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty(null, "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addProperty_ThrowsAnException_WhenTheNameIsEmpty() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty(" ", "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addProperty_ThrowsAnException_WhenTheValueIsNull() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty("name", null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addProperty_ThrowsAnException_WhenTheValueIsEmpty() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty("name", " ");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addProperty_AddsTheProperty_WhenANameAndValueAreSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addProperty("AWS region", "us-east-1");
        assertEquals("us-east-1", element.getProperties().get("AWS region"));
    }

    @Test
    public void test_setProperties_DoesNothing_WhenNullIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setProperties(null);
        assertEquals(0, element.getProperties().size());
    }

    @Test
    public void test_setProperties_SetsTheProperties_WhenANonEmptyMapIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        Map<String, String> properties = new HashMap<>();
        properties.put("name", "value");
        element.setProperties(properties);
        assertEquals(1, element.getProperties().size());
        assertEquals("value", element.getProperties().get("name"));
    }

}