package sml.instructions;

import sml.Instruction;
import sml.Machine;

/**
 * This class ....
 * 
 * @author Fatos Sopjani
 */

public class OutInstruction extends Instruction {

	private int registerID;

	public OutInstruction(String label, String op) {
		super(label, op);
	}

	public OutInstruction(String label, int registerID) {
		this(label, "out");
		this.registerID = registerID;
	}

	@Override
	public void execute(Machine m) {
		System.out.println("Content in register "+ registerID +" is: "+m.getRegisters().getRegister(registerID));
	}

	@Override
	public String toString() {
		return super.toString() + " " + registerID;
	}
}