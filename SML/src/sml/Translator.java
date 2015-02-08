package sml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
/**
 * @author fatos
 *
 */
public class Translator {

	// word + line is the part of the current line that's not yet processed
	// word has no whitespace
	// If word and line are not empty, line begins with whitespace
	private String line = "";
	private Labels labels; // The labels of the program being translated
	private ArrayList<Instruction> program; // The program to be created
	private String fileName; // source file of SML code
	private String wholeLine ="";
	private static final String SRC = "resources";
	private static final String STRING_TYPE = "java.lang.String";
	private static final String INT_TYPE 	= "int";

	public Translator(String fileName) {
		this.fileName = SRC + "/" + fileName;
	}

	// translate the small program in the file into lab (the labels) and
	// prog (the program)
	// return "no errors were detected"
	public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

		try (Scanner sc = new Scanner(new File(fileName))) {
			// Scanner attached to the file chosen by the user
			labels = lab;
			labels.reset();
			program = prog;
			program.clear();

			try {
				line = sc.nextLine();
			} catch (NoSuchElementException ioE) {
				return false;
			}

			// Each iteration processes line and reads the next line into line
			while (line != null) {
				// Store the label in label
				wholeLine = line;
				String label = scan();
				if (label.length() > 0) {
					Instruction ins = getInstruction(label);
					if (ins != null) {
						labels.addLabel(label);
						program.add(ins);
					}
				}

				try {
					line = sc.nextLine();
				} catch (NoSuchElementException ioE) {
					return false;
				}
			}
		} catch (IOException ioE) {
			System.out.println("File: IO error " + ioE.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * @param label
	 * @return
	 */
	public Instruction getInstruction(String label) {
		if (line.equals(""))
			return null;
		String ins = getMachineInstructions("instruction");
		int noOfArgs =0;
		try {
			Class<?> cls = Class.forName(getClassName(ins));			
			Constructor<?>[] constructors = cls.getConstructors();
			for(Constructor<?> ctr : constructors){
				Class<?>[] consPropTypes = ctr.getParameterTypes();
				noOfArgs = getNoOfArgs();
				if(noOfArgs == consPropTypes.length){
					Object[] argsObj = appendObjectArgs(consPropTypes);
					return (Instruction)ctr.newInstance(argsObj);
				}
			}

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SecurityException | IllegalArgumentException | InvocationTargetException  e) {
			e.printStackTrace();
		}

		// You will have to write code here for the other instructions.

		return null;
	}

	// split whole line  counts the length minus the label
	/**
	 * @return
	 */
	private int getNoOfArgs() {
		String[] argumentList = wholeLine.split(" ");
		return argumentList.length-1;
	}
	
	/**
	 * @param ins
	 * @return
	 */
	private String getMachineInstructions(String ins){
		String[] line = wholeLine.split(" "); 
		switch (ins) {
		case "label": return line[0];
		case "instruction": return line[1];
		}
		return null;
	}
	
	/**
	 * @param conPropTypes
	 * @param ins
	 * @param noOfArgs
	 * @return
	 */
	private Object[] appendObjectArgs(Class<?>[] conPropTypes) {
		int intVal = 0;
		String strVAl = null;
		ArrayList<Object> temp = new ArrayList<Object>();
		for(int i =0; i< conPropTypes.length;i++){
			System.out.println("c: "+ conPropTypes[i].getName());
			if(conPropTypes[i].getName().equalsIgnoreCase(INT_TYPE)){
				intVal = scanInt();
				temp.add(intVal);
			}else if(conPropTypes[i].getName().equalsIgnoreCase(STRING_TYPE)){
				strVAl = scan();
				temp.add(strVAl);
			}
			System.out.println("int:" + intVal + " str: " +strVAl);
		}
		return temp.toArray();
	}

	/*
	 * Return the first word of line and remove it from line. If there is no
	 * word, return ""
	 */
	private String scan() {
		line = line.trim();
		if (line.length() == 0)
			return "";

		int i = 0;
		while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
			i = i + 1;
		}
		String word = line.substring(0, i);
		line = line.substring(i);
		return word;
	}

	// Return the first word of line as an integer. If there is
	// any error, return the maximum int
	private int scanInt() {
		String word = scan();
		if (word.length() == 0) {
			return Integer.MAX_VALUE;
		}

		try {
			return Integer.parseInt(word);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}
	
	/**
	 * @param ins
	 * @return
	 */
	private String getClassName(String ins) {
		Properties prop = new Properties();
		String propFileName = "instructions.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		String className="";
		try {
			if (inputStream != null) {
				prop.load(inputStream);
				className = prop.getProperty(ins);
			}else{
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return className;
	}

}