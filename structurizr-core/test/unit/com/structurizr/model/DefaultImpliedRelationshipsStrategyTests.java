package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultImpliedRelationshipsStrategyTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_createImpliedRelationships_DoesNothing() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa = aa.addComponent("AAA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");
        Component bbb = bb.addComponent("BBB", "", "");

        model.setImpliedRelationshipsStrategy(new DefaultImpliedRelationshipsStrategy());

        aaa.uses(bbb, "Uses 1");
        aaa.uses(bbb, "Uses 2");

        assertEquals(2, model.getRelationships().size());
        assertTrue(aaa.hasEfferentRelationshipWith(bbb, "Uses 1"));
        assertTrue(aaa.hasEfferentRelationshipWith(bbb, "Uses 2"));
    }

}