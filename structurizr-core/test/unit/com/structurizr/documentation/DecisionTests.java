package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DecisionTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_hasLinkTo() {
        Decision d1 = new Decision("1");
        Decision d2 = new Decision("2");
        Decision d3 = new Decision("3");

        d1.addLink(d2, "Type");

        assertTrue(d1.hasLinkTo(d2));
        assertFalse(d1.hasLinkTo(d3));
    }

}