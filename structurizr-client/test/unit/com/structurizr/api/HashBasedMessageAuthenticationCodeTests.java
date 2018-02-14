package com.structurizr.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashBasedMessageAuthenticationCodeTests {

    private HashBasedMessageAuthenticationCode code;

    @Test
    public void test_generate() throws Exception {
        // this example is taken from http://en.wikipedia.org/wiki/Hash-based_message_authentication_code
        code = new HashBasedMessageAuthenticationCode("key");
        assertEquals("f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8", code.generate("The quick brown fox jumps over the lazy dog"));
    }

}