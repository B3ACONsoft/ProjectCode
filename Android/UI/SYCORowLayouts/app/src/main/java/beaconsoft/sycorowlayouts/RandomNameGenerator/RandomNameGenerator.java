package beaconsoft.sycorowlayouts.RandomNameGenerator;
/*
WordNet 3.0 license: (Download)

WordNet Release 3.0 This software and database is being provided to you, the LICENSEE, by
Princeton University under the following license. By obtaining, using and/or copying this software
and database, you agree that you have read, understood, and will comply with these terms and
conditions.: Permission to use, copy, modify and distribute this software and database and its
documentation for any purpose and without fee or royalty is hereby granted, provided that you
agree to comply with the following copyright notice and statements, including the disclaimer, and
that the same appear on ALL copies of the software, database and documentation, including
modifications that you make for internal use or for distribution. WordNet 3.0 Copyright 2006 by
Princeton University. All rights reserved. THIS SOFTWARE AND DATABASE IS PROVIDED "AS IS"
AND PRINCETON UNIVERSITY MAKES NO REPRESENTATIONS OR WARRANTIES, EXPRESS
OR IMPLIED. BY WAY OF EXAMPLE, BUT NOT LIMITATION, PRINCETON UNIVERSITY MAKES
NO REPRESENTATIONS OR WARRANTIES OF MERCHANT- ABILITY OR FITNESS FOR ANY
PARTICULAR PURPOSE OR THAT THE USE OF THE LICENSED SOFTWARE, DATABASE OR
DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS,
TRADEMARKS OR OTHER RIGHTS. The name of Princeton University or Princeton may not be
used in advertising or publicity pertaining to distribution of the software and/or database. Title to
copyright in this software, database and any associated documentation shall at all times remain
with Princeton University and LICENSEE agrees to preserve same.
*/

/**
 * Generates pseudo random unique names that combines one adjective and one noun,
 * like "friendly tiger" or "good apple".
 *
 * There's about 1.5 million unique combinations, and if you keep requesting a new word
 * it will start to loop (but this code will generate all unique combinations before it starts
 * to loop.)
 *
 * @author Kohsuke Kawaguchi
 */
public class RandomNameGenerator {
    private int pos;

    public RandomNameGenerator(int seed) {
        this.pos = seed;
    }

    public RandomNameGenerator() {
        this((int) System.currentTimeMillis());
    } 
    public synchronized String next() {
        Dictionary d = Dictionary.INSTANCE;
        pos = Math.abs(pos+d.getPrime()) % d.size();
        return d.word(pos);
    }
}
