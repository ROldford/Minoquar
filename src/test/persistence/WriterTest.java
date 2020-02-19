// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package persistence;

import model.MazeListModel;
import model.MazeModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriterTest {
    // TODO: implement this
    // idea: generate test data by reading existing test save file
    //       then re-save to new file and read back

    private static final String TEST_READ_FILE = "./data/test/testMazeList3.txt";
    private static final String TEST_WRITE_FILE = "./data/test/testMazeListWrite.txt";
    private MazeListModel testData;
    private Writer testWriter;

    @BeforeEach
    void runBefore() throws IOException  {
        testData = Reader.readMazeList(new File(TEST_READ_FILE));
        testWriter = new Writer(new File(TEST_WRITE_FILE));
    }

    @Test
    void testWriteMazeList() {
        testWriter.write(testData);
        testWriter.close();
        try {
            MazeListModel writeResult = Reader.readMazeList(new File(TEST_WRITE_FILE));
            testWriteResultMatchesTestData(testData.readMaze(0), writeResult.readMaze(0));
            testWriteResultMatchesTestData(testData.readMaze(1), writeResult.readMaze(1));
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    private void testWriteResultMatchesTestData(MazeModel expected, MazeModel actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSizeName(), actual.getSizeName());
        List<String> expectedSaveData = expected.getSaveData();
        List<String> actualSaveData = actual.getSaveData();
        Utilities.iterateSimultaneously(
                expectedSaveData, actualSaveData,
                Assertions::assertEquals);
    }


}