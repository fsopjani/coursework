package test.suite;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sml.Translator;
import sml.instructions.LinInstruction;

public class TranslatorTest {

	@Test
	public void testGetInstruction() {
		
		Translator t = new Translator("instrcutions.txt");
		String actual = t.getInstruction("f0 lin 20 6").toString();
		String expected = new LinInstruction("lin", 20, 6).toString();
		assertEquals(expected,actual);
	}

}
