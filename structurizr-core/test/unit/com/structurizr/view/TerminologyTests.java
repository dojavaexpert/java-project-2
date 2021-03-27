package com.structurizr.view;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TerminologyTests {

    @Test
    public void test_findTerminology() {
        Workspace workspace = new Workspace("Name", "Description");
        Terminology terminology = workspace.getViews().getConfiguration().getTerminology();
        Person person = workspace.getModel().addPerson("Name");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);
        Relationship relationship = person.uses(softwareSystem, "Uses");

        assertEquals("Person", terminology.findTerminology(person));
        assertEquals("Software System", terminology.findTerminology(softwareSystem));
        assertEquals("Container", terminology.findTerminology(container));
        assertEquals("Component", terminology.findTerminology(component));
        assertEquals("Deployment Node", terminology.findTerminology(deploymentNode));
        assertEquals("Infrastructure Node", terminology.findTerminology(infrastructureNode));
        assertEquals("Software System", terminology.findTerminology(softwareSystemInstance));
        assertEquals("Container", terminology.findTerminology(containerInstance));
        assertEquals("Relationship", terminology.findTerminology(relationship));

        terminology.setPerson("PERSON");
        terminology.setSoftwareSystem("SOFTWARE SYSTEM");
        terminology.setContainer("CONTAINER");
        terminology.setComponent("COMPONENT");
        terminology.setDeploymentNode("DEPLOYMENT NODE");
        terminology.setInfrastructureNode("INFRASTRUCTURE NODE");
        terminology.setRelationship("RELATIONSHIP");

        assertEquals("PERSON", terminology.findTerminology(person));
        assertEquals("SOFTWARE SYSTEM", terminology.findTerminology(softwareSystem));
        assertEquals("CONTAINER", terminology.findTerminology(container));
        assertEquals("COMPONENT", terminology.findTerminology(component));
        assertEquals("DEPLOYMENT NODE", terminology.findTerminology(deploymentNode));
        assertEquals("INFRASTRUCTURE NODE", terminology.findTerminology(infrastructureNode));
        assertEquals("SOFTWARE SYSTEM", terminology.findTerminology(softwareSystemInstance));
        assertEquals("CONTAINER", terminology.findTerminology(containerInstance));
        assertEquals("RELATIONSHIP", terminology.findTerminology(relationship));
    }

}