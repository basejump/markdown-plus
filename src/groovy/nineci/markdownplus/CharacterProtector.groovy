/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus
 */

package nineci.markdownplus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


class CharacterProtector {
    def protectMap = [:]
    def unprotectMap = [:]

    String encode(String literal) {
        if (!protectMap.containsKey(literal)) {
            addToken(literal)
        }
        return protectMap[literal]
    }

    String decode(String coded) {
        return unprotectMap[coded]
    }

    Collection<String> getAllEncodedTokens() {
        return unprotectMap.keySet();
    }

    private void addToken(String literal) {
        String uuid = UUID.randomUUID().toString()
        protectMap[literal]= uuid
        unprotectMap[uuid]= literal
    }

    public String toString() {
        return protectMap.toString();
    }
}
