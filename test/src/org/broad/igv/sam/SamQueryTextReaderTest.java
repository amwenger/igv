/*
 * Copyright (c) 2007-2011 by The Broad Institute of MIT and Harvard.  All Rights Reserved.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 *
 * THE SOFTWARE IS PROVIDED "AS IS." THE BROAD AND MIT MAKE NO REPRESENTATIONS OR
 * WARRANTES OF ANY KIND CONCERNING THE SOFTWARE, EXPRESS OR IMPLIED, INCLUDING,
 * WITHOUT LIMITATION, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, NONINFRINGEMENT, OR THE ABSENCE OF LATENT OR OTHER DEFECTS, WHETHER
 * OR NOT DISCOVERABLE.  IN NO EVENT SHALL THE BROAD OR MIT, OR THEIR RESPECTIVE
 * TRUSTEES, DIRECTORS, OFFICERS, EMPLOYEES, AND AFFILIATES BE LIABLE FOR ANY DAMAGES
 * OF ANY KIND, INCLUDING, WITHOUT LIMITATION, INCIDENTAL OR CONSEQUENTIAL DAMAGES,
 * ECONOMIC DAMAGES OR INJURY TO PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER
 * THE BROAD OR MIT SHALL BE ADVISED, SHALL HAVE OTHER REASON TO KNOW, OR IN FACT
 * SHALL KNOW OF THE POSSIBILITY OF THE FOREGOING.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.broad.igv.sam;

import net.sf.samtools.util.CloseableIterator;
import org.broad.igv.Globals;
import org.broad.igv.sam.reader.AlignmentIndexer;
import org.broad.igv.sam.reader.SAMReader;
import org.broad.igv.util.TestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author jrobinso
 */
public class SamQueryTextReaderTest {

    public SamQueryTextReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Globals.setHeadless(true);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    private void createSamIndex(String samfile) throws IOException {
        AlignmentIndexer.getInstance(new File(samfile), null, null).createSamIndex();
    }

    /**
     * Test of query method, of class SamQueryTextReader.
     */
    @Test
    public void testQuery() throws Exception {

        String testFile = TestUtils.DATA_DIR + "/sam/NA12878.muc1.test.sam";
        createSamIndex(testFile);

        String chr = "chr1";
        int start = 153426040;
        int end = 153426154;

        // Test posA query that includes overlaps (contained == false)
        boolean contained = false;
        SAMReader reader = new SAMReader(testFile);
        CloseableIterator<Alignment> iter = reader.query(chr, start, end, contained);
        int count = 0;
        while (iter.hasNext()) {
            Alignment record = iter.next();
            assertEquals(chr, record.getChr());
            assertTrue(record.getEnd() >= start);
            assertTrue(record.getStart() <= end);
            count++;
        }
        assertEquals(64, count);
        iter.close();
        reader.close();
    }

    /**
     * Test of query method, of class SamQueryTextReader.
     * <p/>
     * Regression test for RT 134402.
     */
    @Test
    public void testQuery2() throws Exception {

        String testFile = TestUtils.DATA_DIR + "/sam/test_2_plus_one_read.sam";
        createSamIndex(testFile);

        //chr3:125,963,167-125,972,750
        String chr = "chr3";
        int start = 125963167;
        int end = 125972750;

        // Test posA query that includes overlaps (contained == false)
        boolean contained = false;
        SAMReader reader = new SAMReader(testFile);
        CloseableIterator<Alignment> iter = reader.query(chr, start, end, contained);
        int count = 0;
        while (iter.hasNext()) {
            Alignment record = iter.next();
            assertEquals(chr, record.getChr());
            assertTrue(record.getEnd() >= start);
            assertTrue(record.getStart() <= end);
            count++;
        }
        assertEquals(329, count);
        iter.close();
        reader.close();

    }

    /**
     * Test of query method, of class SamQueryTextReader.
     * <p/>
     * Regression test for RT 134339.
     */
    @Test
    public void testQuery3() throws Exception {

        String testFile = TestUtils.DATA_DIR + "/sam/test_minus_converted.sam";
        createSamIndex(testFile);

        //chr3:125,963,167-125,972,750
        String chr = "chr1";
        int start = 12550532;
        int end = 12550610;

        // Test posA query that includes overlaps (contained == false)
        boolean contained = false;
        SAMReader reader = new SAMReader(testFile);
        CloseableIterator<Alignment> iter = reader.query(chr, start, end, contained);
        int count = 0;
        while (iter.hasNext()) {
            Alignment record = iter.next();
            assertEquals(chr, record.getChr());
            assertTrue(record.getEnd() >= start);
            assertTrue(record.getStart() <= end);
            count++;
        }
        assertEquals(2, count);
        iter.close();
        reader.close();

    }

    @Test
    public void testMoran() throws Exception {
        String testFile = TestUtils.LARGE_DATA_DIR + "/r2.allProb.sorted.sam";
        String chr = "mm9chrY";
        int start = 799939;
        int end = 800152;

        // Test posA query that includes overlaps (contained == false)
        boolean contained = false;

        SAMReader reader = new SAMReader(testFile);
        CloseableIterator<Alignment> iter = reader.query(chr, start, end, contained);
        int count = 0;
        while (iter.hasNext()) {
            Alignment record = iter.next();
            assertEquals(chr, record.getChr());
            assertTrue(record.getEnd() >= start);
            assertTrue(record.getStart() <= end);
            count++;
        }
        assertEquals(134, count);
        iter.close();
        reader.close();
    }


}
