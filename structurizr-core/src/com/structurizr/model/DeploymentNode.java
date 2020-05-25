package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * <p>
 *   Represents a deployment node, which is something like:
 * </p>
 *
 * <ul>
 *     <li>Physical infrastructure (e.g. a physical server or device)</li>
 *     <li>Virtualised infrastructure (e.g. IaaS, PaaS, a virtual machine)</li>
 *     <li>Containerised infrastructure (e.g. a Docker container)</li>
 *     <li>Database server</li>
 *     <li>Java EE web/application server</li>
 *     <li>Microsoft IIS</li>
 *     <li>etc</li>
 * </ul>
 */
public final class DeploymentNode extends DeploymentElement {

    private DeploymentNode parent;
    private String technology;
    private int instances = 1;

    private Set<DeploymentNode> children = new HashSet<>();
    private Set<InfrastructureNode> infrastructureNodes = new HashSet<>();
    private Set<ContainerInstance> containerInstances = new HashSet<>();

    /**
     * Adds a container instance to this deployment node, replicating all of the container-container relationships.
     *
     * @param container     the Container to add an instance of
     * @return  a ContainerInstance object
     */
    public ContainerInstance add(Container container) {
        return add(container, true);
    }

    /**
     * Adds a container instance to this deployment node, optionally replicating all of the container-container relationships.
     *
     * @param container                         the Container to add an instance of
     * @param replicateContainerRelationships   true if the container-container relationships should be replicated between the container instances, false otherwise
     * @return  a ContainerInstance object
     */
    public ContainerInstance add(Container container, boolean replicateContainerRelationships) {
        ContainerInstance containerInstance = getModel().addContainerInstance(this, container, replicateContainerRelationships);
        this.containerInstances.add(containerInstance);

        return containerInstance;
    }

    /**
     * Adds a child deployment node.
     *
     * @param name          the name of the deployment node
     * @return  a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name) {
        return addDeploymentNode(name, null, null);
    }

    /**
     * Adds a child deployment node.
     *
     * @param name          the name of the deployment node
     * @param description   a short description
     * @param technology    the technology
     * @return  a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology) {
        return addDeploymentNode(name, description, technology, 1);
    }

    /**
     * Adds a child deployment node.
     *
     * @param name          the name of the deployment node
     * @param description   a short description
     * @param technology    the technology
     * @param instances     the number of instances
     * @return  a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances) {
        return addDeploymentNode(name, description, technology, instances, null);
    }

    /**
     * Adds a child deployment node.
     *
     * @param name          the name of the deployment node
     * @param description   a short description
     * @param technology    the technology
     * @param instances     the number of instances
     * @param properties    a Map (String,String) describing name=value properties
     * @return  a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances, Map<String, String> properties) {
        DeploymentNode deploymentNode = getModel().addDeploymentNode(this, this.getEnvironment(), name, description, technology, instances, properties);
        if (deploymentNode != null) {
            children.add(deploymentNode);
        }
        return deploymentNode;
    }

    /**
     * Gets the DeploymentNode with the specified name.
     *
     * @param name the name of the deployment node
     * @return the DeploymentNode instance with the specified name (or null if it doesn't exist).
     */
    public DeploymentNode getDeploymentNodeWithName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        for (DeploymentNode deploymentNode : getChildren()) {
            if (deploymentNode.getName().equals(name)) {
                return deploymentNode;
            }
        }

        return null;
    }

    /**
     * Gets the infrastructure node with the specified name.
     *
     * @param name      the name of the infrastructure node
     * @return          the InfrastructureNode instance with the specified name (or null if it doesn't exist).
     */
    public InfrastructureNode getInfrastructureNodeWithName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        for (InfrastructureNode infrastructureNode : getInfrastructureNodes()) {
            if (infrastructureNode.getName().equals(name)) {
                return infrastructureNode;
            }
        }

        return null;
    }

    /**
     * Adds a child infrastructure node.
     *
     * @param name          the name of the infrastructure node
     * @return              an InfrastructureNode object
     */
    public InfrastructureNode addInfrastructureNode(String name) {
        return addInfrastructureNode(name, null, null);
    }

    /**
     * Adds a child infrastructure node.
     *
     * @param name          the name of the infrastructure node
     * @param description   a short description
     * @param technology    the technology
     * @return              an InfrastructureNode object
     */
    public InfrastructureNode addInfrastructureNode(String name, String description, String technology) {
        return addInfrastructureNode(name, description, technology, null);
    }

    /**
     * Adds a child infrastructure node.
     *
     * @param name          the name of the infrastructure node
     * @param description   a short description
     * @param technology    the technology
     * @param properties    a Map (String,String) describing name=value properties
     * @return              an InfrastructureNode object
     */
    public InfrastructureNode addInfrastructureNode(String name, String description, String technology, Map<String, String> properties) {
        InfrastructureNode infrastructureNode = getModel().addInfrastructureNode(this, name, description, technology, properties);
        if (infrastructureNode != null) {
            infrastructureNodes.add(infrastructureNode);
        }
        return infrastructureNode;
    }

    /**
     * Adds a relationship between this and another deployment node.
     *
     * @param destination   the destination DeploymentNode
     * @param description   a short description of the relationship
     * @param technology    the technology
     * @return              a Relationship object
     */
    public Relationship uses(DeploymentNode destination, String description, String technology) {
        return uses(destination, description, technology, InteractionStyle.Synchronous);
    }

    /**
     * Adds a relationship between this and another deployment node.
     *
     * @param destination       the destination DeploymentNode
     * @param description       a short description of the relationship
     * @param technology        the technology
     * @param interactionStyle  the interaction style (Synchronous vs Asynchronous)
     * @return                  a Relationship object
     */
    public Relationship uses(DeploymentNode destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Gets the set of child deployment nodes.
     *
     * @return  a Set of DeploymentNode objects
     */
    public Set<DeploymentNode> getChildren() {
        return new HashSet<>(children);
    }

    void setChildren(Set<DeploymentNode> children) {
        if (children != null) {
            this.children = new HashSet<>(children);
        }
    }

    /**
     * Gets the set of child infrastructure nodes.
     *
     * @return  a Set of InfrastructureNode objects
     */
    public Set<InfrastructureNode> getInfrastructureNodes() {
        return new HashSet<>(infrastructureNodes);
    }

    void setInfrastructureNodes(Set<InfrastructureNode> infrastructureNodes) {
        if (infrastructureNodes != null) {
            this.infrastructureNodes = new HashSet<>(infrastructureNodes);
        }
    }

    @JsonIgnore
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Gets the set of container instances associated with this deployment node.
     *
     * @return  a Set of ContainerInstance objects
     */
    public Set<ContainerInstance> getContainerInstances() {
        return new HashSet<>(containerInstances);
    }

    void setContainerInstances(Set<ContainerInstance> containerInstances) {
        if (containerInstances != null) {
            this.containerInstances = new HashSet<>(containerInstances);
        }
    }

    /**
     * Gets the parent deployment node.
     *
     * @return  the parent DeploymentNode, or null if there is no parent
     */
    @Override
    @JsonIgnore
    public Element getParent() {
        return parent;
    }

    void setParent(DeploymentNode parent) {
        this.parent = parent;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    @JsonIgnore
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.DEPLOYMENT_NODE));
    }

    @Override
    public String getCanonicalName() {
        if (getParent() != null) {
            return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
        } else {
            return CANONICAL_NAME_SEPARATOR + "Deployment" + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getEnvironment()) + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
        }
    }

}