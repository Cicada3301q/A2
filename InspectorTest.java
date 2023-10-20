//NOTE: the code does not really lend itself to unit testing, but i have just made some mock unit tests for testing if output stream contains expected strings.

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ObjectInspectorTest {
    private ObjectInspector inspector;
    private OutputCapture outputCapture;

    @Before
    public void setUp() {
        inspector = new ObjectInspector();
        outputCapture = new OutputCapture();
    }

    @Test
    public void testInspectFields() {
        MockClassA obj = new MockClassA();
        inspector.inspect(obj, false);
        outputCapture.stopCapture();
        String capturedOutput = outputCapture.getCapturedOutput();

        // Check if the captured output contains "Field Name: val"
        assertTrue(capturedOutput.contains("Field Name: val"));
        assertTrue(capturedOutput.contains("Field Name: val2"));
        assertTrue(capturedOutput.contains("Field Name: val3"));
        assertTrue(capturedOutput.contains("Field Value: 3"));
    }

    @Test
    public void testInspectMethod() {
        MockClassA obj = new MockClassA();
        inspector.inspect(obj, false);
        outputCapture.stopCapture();
        String capturedOutput = outputCapture.getCapturedOutput();

        // Check if the captured output contains expected strings
        assertTrue(capturedOutput.contains("Method Name: : run"));
        assertTrue(capturedOutput.contains("Method Name: : getVal"));
        assertTrue(capturedOutput.contains("Method Name: : setVal"));

        assertTrue(capturedOutput.contains("Return Type: : void"));
        assertTrue(capturedOutput.contains("Method Name: : int"));
    }

    @Test
    public void testprintParameterTypes() {
        MockClassA obj = new MockClassA();
        inspector.inspect(obj, false);
        outputCapture.stopCapture();
        String capturedOutput = outputCapture.getCapturedOutput();

        // Check if the captured output contains expected strings
        assertTrue(capturedOutput.contains("Parameter Types: None"));

    }

    @Test
    public void testInspectSuperClasses() {
        MockClassA obj = new MockClassA();
        inspector.inspect(obj, false);
        outputCapture.stopCapture();
        String capturedOutput = outputCapture.getCapturedOutput();

        // Check if the captured output contains expected strings
        assertTrue(capturedOutput.contains("java.lang.Object"));

    }

    @After
    public void tearDown() {
        outputCapture.stopCapture();
    }

class MockClassA {
    public MockClassA() {
        val = 3;
    }

    public MockClassA(int i) {

        try {
            setVal(i);
        } catch (Exception e) {
        }
    }

    public void run() {
    }

    public int getVal() {
        return val;
    }

    public void setVal(int i) throws Exception {
        if (i < 0)
            throw new Exception("negative value");

        val = i;
    }

    public String toString() {
        return "ClassA";
    }

    private void printSomething() {
        System.out.println("Something");
    }

    private int val = 3;
    private double val2 = 0.2;
    private boolean val3 = true;
}