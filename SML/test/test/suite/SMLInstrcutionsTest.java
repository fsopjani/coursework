package test.suite;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sml.instructions.SMLInstrcutions;

public class SMLInstrcutionsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testSMLInstrcutions() {
		SMLInstrcutions sml = new SMLInstrcutions("f0 lin 20 6");
		assertEquals("f0 lin 20 6", sml.getLine());
	}

	@Test
	public void testGetConstructionArgs() {
		SMLInstrcutions sml = new SMLInstrcutions("f0 lin 20 6");
		Object[] actual = sml.getConstructionArgs();
		assertArrayEquals(new Object[]{"lin",20,6}, actual); 
	}

	@Test
	public void testGetNumberOfArgs() {
		SMLInstrcutions sml = new SMLInstrcutions("f0 lin 20 6");
		sml.getConstructionArgs();
		assertEquals(3,sml.getNumberOfConsArgs());
	}

	@Test
	public void testCheckInstructionType() {
//		fail("Not yet implemented");
	}
}
