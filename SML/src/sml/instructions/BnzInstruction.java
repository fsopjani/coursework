package sml.instructions;

import sml.Instruction;
import sml.Machine;

/**
 * This class ....
 * 
 * @author someone
 */

public class BnzInstruction extends Instruction {
	private int register;
	private int value;

	public BnzInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public BnzInstruction(String label, int register, int value) {
		super(label, "lin");
		this.register = register;
		this.value = value;

	}

	@Override
	public void execute(Machine m) {
		m.getRegisters().setRegister(register, value);
	}

	@Override
	public String toString() {
		return super.toString() + " register " + register + " value is " + value;
	}
}
