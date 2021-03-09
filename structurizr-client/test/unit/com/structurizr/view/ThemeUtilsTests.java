package com.structurizr.view;

import com.structurizr.Workspace;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class ThemeUtilsTests {

    @Test
    public void test_loadThemes_DoesNothingWhenNoThemesAreDefined() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        ThemeUtils.loadThemes(workspace);

        // there should still be zero styles in the workspace
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getElements().size());
    }

    @Test
    public void test_loadThemes_LoadsThemesWhenThemesAreDefined() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        softwareSystem.addTags("Amazon Web Services - Alexa For Business");
        workspace.getViews().getConfiguration().setThemes("https://static.structurizr.com/themes/amazon-web-services-2020.04.30/theme.json");

        ThemeUtils.loadThemes(workspace);

        // there should still be zero styles in the workspace
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getElements().size());

        // but we should be able to find a style included in the theme
        ElementStyle style = workspace.getViews().getConfiguration().getStyles().findElementStyle(softwareSystem);
        assertNotNull(style);
        assertEquals("#d6242d", style.getStroke());
        assertEquals("#d6242d", style.getColor());
        assertNotNull(style.getIcon());
    }

    @Test
    public void test_toJson() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        assertEquals("{\n" +
                "  \"name\" : \"Name\",\n" +
                "  \"description\" : \"Description\"\n" +
                "}", ThemeUtils.toJson(workspace));

        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.ELEMENT).background("#ff0000");
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
        assertEquals("{\n" +
                "  \"name\" : \"Name\",\n" +
                "  \"description\" : \"Description\",\n" +
                "  \"elements\" : [ {\n" +
                "    \"tag\" : \"Element\",\n" +
                "    \"background\" : \"#ff0000\"\n" +
                "  } ],\n" +
                "  \"relationships\" : [ {\n" +
                "    \"tag\" : \"Relationship\",\n" +
                "    \"color\" : \"#ff0000\"\n" +
                "  } ]\n" +
                "}", ThemeUtils.toJson(workspace));
    }

    @Test
    public void test_findElementStyle_WithThemes() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Element").shape(Shape.RoundedBox);

        // theme 1
        Collection<ElementStyle> elementStyles = new ArrayList<>();
        Collection<RelationshipStyle> relationshipStyles = new ArrayList<>();
        elementStyles.add(new ElementStyle("Element").shape(Shape.Box).background("#000000").color("#ffffff"));
        workspace.getViews().getConfiguration().getStyles().addStylesFromTheme("url1", elementStyles, relationshipStyles);

        // theme 2
        elementStyles = new ArrayList<>();
        relationshipStyles = new ArrayList<>();
        elementStyles.add(new ElementStyle("Element").background("#ff0000"));
        workspace.getViews().getConfiguration().getStyles().addStylesFromTheme("url2", elementStyles, relationshipStyles);

        ElementStyle style = workspace.getViews().getConfiguration().getStyles().findElementStyle(softwareSystem);
        assertEquals(new Integer(450), style.getWidth());
        assertEquals(new Integer(300), style.getHeight());
        assertEquals("#ff0000", style.getBackground()); // from theme 2
        assertEquals("#ffffff", style.getColor()); // from theme 1
        assertEquals(new Integer(24), style.getFontSize());
        assertEquals(Shape.RoundedBox, style.getShape()); // from workspace
        assertNull(style.getIcon());
        assertEquals(Border.Solid, style.getBorder());
        assertEquals("#dddddd", style.getStroke());
        assertEquals(new Integer(100), style.getOpacity());
        assertEquals(true, style.getMetadata());
        assertEquals(true, style.getDescription());
    }

    @Test
    public void test_findRelationshipStyle_WithThemes() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        Relationship relationship = softwareSystem.uses(softwareSystem, "Uses");
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle("Relationship").dashed(false);

        // theme 1
        Collection<ElementStyle> elementStyles = new ArrayList<>();
        Collection<RelationshipStyle> relationshipStyles = new ArrayList<>();
        relationshipStyles.add(new RelationshipStyle("Relationship").color("#ff0000").thickness(4));
        workspace.getViews().getConfiguration().getStyles().addStylesFromTheme("url1", elementStyles, relationshipStyles);

        // theme 2
        elementStyles = new ArrayList<>();
        relationshipStyles = new ArrayList<>();
        relationshipStyles.add(new RelationshipStyle("Relationship").color("#0000ff"));
        workspace.getViews().getConfiguration().getStyles().addStylesFromTheme("url2", elementStyles, relationshipStyles);

        RelationshipStyle style = workspace.getViews().getConfiguration().getStyles().findRelationshipStyle(relationship);
        assertEquals(new Integer(4), style.getThickness()); // from theme 1
        assertEquals("#0000ff", style.getColor()); // from theme 2
        Assert.assertFalse(style.getDashed()); // from workspace
        assertEquals(Routing.Direct, style.getRouting());
        assertEquals(new Integer(24), style.getFontSize());
        assertEquals(new Integer(200), style.getWidth());
        assertEquals(new Integer(50), style.getPosition());
        assertEquals(new Integer(100), style.getOpacity());
    }

}