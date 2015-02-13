package sml.instructions;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class SMLInstrcutions {
	private static final String STRING_TYPE = "java.lang.String";
	private static final String INT_TYPE 	= "int";
	private static final String INTEGER_TYPE = "java.lang.Integer";

	private String line;
	private String label;
	private String instruction;
	private List<Object> constructorArgs;
	
	public SMLInstrcutions(String line){
		this.line = line;
		assignInstructions();
	}

	private void assignInstructions() {
		String[] lineArray = line.split(" ");
		setLabel(lineArray[0]);
		setInstruction(lineArray[1]);
	}
	
	public Object[] getConstructionArgs(){
		constructorArgs= new ArrayList<Object>();
		String[] lines = line.split(" ");
		for(int i =1;i<lines.length;i++){
			if(isInteger(lines[i])){
				constructorArgs.add(Integer.parseInt(lines[i]));
			}else {
				constructorArgs.add(lines[i]);			
			}	
		}
		return constructorArgs.toArray();
	}

	public int getNumberOfConsArgs(){
		return constructorArgs.size();
	}

	public boolean checkInstructionType(Class<?>[] consPropTypes) {
		for(int i =0; i<consPropTypes.length;i++){
				String consType="";
				if(consPropTypes[i].getName().equalsIgnoreCase(INT_TYPE)){
					consType = INTEGER_TYPE;
				}else{
					consType = STRING_TYPE;
				}
				if(!consType.equalsIgnoreCase(constructorArgs.get(i).getClass().getName())){
				return false;
				}
		}
		return true;
	}
	
	/**
	 * @param arg
	 * @return
	 */
	public boolean isInteger( String arg ) {
	    try {
	        Integer.parseInt( arg );
	        return true;
	    }
	    catch( Exception e ) {
	        return false;
	    }
	}
	

}
