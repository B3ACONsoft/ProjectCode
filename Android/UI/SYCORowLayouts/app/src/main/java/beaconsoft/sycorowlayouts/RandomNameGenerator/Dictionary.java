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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Dictionary of adjectives and nouns.
 *
 * @author Kohsuke Kawaguchi
 */
public class Dictionary {
    private List<String> nouns = new ArrayList<String>();
    private List<String> adjectives = new ArrayList<String>();

    private final int prime;

    public Dictionary() {
        try {
            load("a.txt", adjectives);
            load("n.txt", nouns);
        } catch (IOException e) {
            throw new Error(e);
        }

        int combo = size();

        int primeCombo = 2;
        while (primeCombo<=combo) {
            int nextPrime = primeCombo+1;
            primeCombo *= nextPrime;
        }
        prime = primeCombo+1;
    }

    /**
     * Total size of the combined words.
     */
    public int size() {
        return nouns.size()*adjectives.size();
    }

    /**
     * Sufficiently big prime that's bigger than {@link #size()}
     */
    public int getPrime() {
        return prime;
    }

    public String word(int i) {
        int a = i%adjectives.size();
        int n = i/adjectives.size();

        return adjectives.get(a)+"_"+nouns.get(n);
    }

    private void load(String name, List<String> col) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(name),"US-ASCII"));
        try {
            String line;
            while ((line=r.readLine())!=null)
                col.add(line);
        } finally {
            r.close();
        }
    }

    static final Dictionary INSTANCE = new Dictionary();
}
