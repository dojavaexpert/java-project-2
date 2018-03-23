package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContainerInstanceTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System", "Description");
    private Container database = softwareSystem.addContainer("Database Schema", "Stores data", "MySQL");
    private DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");

    @Test
    public void test_construction() {
        ContainerInstance containerInstance = deploymentNode.add(database);

        assertSame(database, containerInstance.getContainer());
        assertEquals(database.getId(), containerInstance.getContainerId());
        assertEquals(1, containerInstance.getInstanceId());
    }

    @Test
    public void test_getContainerId() {
        ContainerInstance containerInstance = deploymentNode.add(database);

        assertEquals(database.getId(), containerInstance.getContainerId());
        containerInstance.setContainer(null);
        containerInstance.setContainerId("1234");
        assertEquals("1234", containerInstance.getContainerId());
    }

    @Test
    public void test_getName() {
        ContainerInstance containerInstance = deploymentNode.add(database);

        assertNull(containerInstance.getName());

        containerInstance.setName("foo");
        assertNull(containerInstance.getName());
    }

    @Test
    public void test_getCanonicalName() {
        ContainerInstance containerInstance = deploymentNode.add(database);

        assertEquals("/System/Database Schema[1]", containerInstance.getCanonicalName());
    }

    @Test
    public void test_getParent_ReturnsTheParentSoftwareSystem() {
        ContainerInstance containerInstance = deploymentNode.add(database);

        assertEquals(softwareSystem, containerInstance.getParent());
    }

    @Test
    public void test_getRequiredTags() {
        ContainerInstance containerInstance = deploymentNode.add(database);

        assertTrue(containerInstance.getRequiredTags().isEmpty());
    }

    @Test
    public void test_getTags() {
        database.addTags("Database");
        ContainerInstance containerInstance = deploymentNode.add(database);
        containerInstance.addTags("Primary Instance");

        assertEquals("Element,Container,Database,Container Instance,Primary Instance", containerInstance.getTags());
    }

    @Test
    public void test_removeTags_DoesNotRemoveAnyTags() {
        ContainerInstance containerInstance = deploymentNode.add(database);

        assertTrue(containerInstance.getTags().contains(Tags.ELEMENT));
        assertTrue(containerInstance.getTags().contains(Tags.CONTAINER));
        assertTrue(containerInstance.getTags().contains(Tags.CONTAINER_INSTANCE));

        containerInstance.removeTag(Tags.CONTAINER_INSTANCE);
        containerInstance.removeTag(Tags.CONTAINER);
        containerInstance.removeTag(Tags.ELEMENT);

        assertTrue(containerInstance.getTags().contains(Tags.ELEMENT));
        assertTrue(containerInstance.getTags().contains(Tags.CONTAINER));
        assertTrue(containerInstance.getTags().contains(Tags.CONTAINER_INSTANCE));
    }

    @Test
    public void test_uses_ThrowsAnException_WhenADestinationIsNotSpecified() {
        ContainerInstance containerInstance = deploymentNode.add(database);

        try {
            containerInstance.uses(null, "", "");
        } catch (IllegalArgumentException iae) {
            assertEquals("The destination of a relationship must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_uses_AddsARelationship_WhenADestinationIsSpecified() {
        Container database = softwareSystem.addContainer("Database", "", "");
        ContainerInstance primaryDatabase =  deploymentNode.add(database);
        ContainerInstance secondaryDatabase = deploymentNode.add(database);

        Relationship relationship = primaryDatabase.uses(secondaryDatabase, "Replicates data to", "Some technology");
        assertSame(primaryDatabase, relationship.getSource());
        assertSame(secondaryDatabase, relationship.getDestination());
        assertEquals("Replicates data to", relationship.getDescription());
        assertEquals("Some technology", relationship.getTechnology());
    }

    @Test
    public void test_addHealthCheck() {
        Container webApplication = softwareSystem.addContainer("Web Application", "", "");
        ContainerInstance instance = deploymentNode.add(webApplication);
        assertTrue(instance.getHealthChecks().isEmpty());

        HttpHealthCheck healthCheck = instance.addHealthCheck("Test web application is working", "http://localhost:8080");
        assertEquals("Test web application is working", healthCheck.getName());
        assertEquals("http://localhost:8080", healthCheck.getUrl());
        assertEquals(1, instance.getHealthChecks().size());
    }

}
