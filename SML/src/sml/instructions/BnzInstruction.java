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
	private String jumpToLabel;

	public BnzInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public BnzInstruction(String label, int register, String jumpToLbl) {
		super(label, "bnz");
		this.register = register;
		this.jumpToLabel = jumpToLbl;

	}

	@Override
	public void execute(Machine m) {
		registerId = m.getRegisters().getRegister(register);
		System.out.println("BNZ register "+ register+ " value "+ jumpToLabel);
		if(registerId!= 0){
			m.setPc(m.getLabels().indexOf(jumpToLabel));
		}
	}

	@Override
	public String toString() {
		return super.toString() + " register " + register + " value is " + jumpToLabel;
	}
}
