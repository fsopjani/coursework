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
	private int registerId;
	private String label2;

	public BnzInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public BnzInstruction(String label, int register, String label2) {
		super(label, "bnz");
		this.register = register;
		this.label2 = label2;

	}

	@Override
	public void execute(Machine m) {
		registerId = m.getRegisters().getRegister(register);
		System.out.println("BNZ register "+ register+ " value "+ label2);
		if(registerId!= 0){
			m.setPc(m.getLabels().indexOf(label2));
		}
	}

	@Override
	public String toString() {
		return super.toString() + " register " + register + " value is " + label2;
	}
}
