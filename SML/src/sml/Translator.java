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

import sml.instructions.SMLInstrcutions;

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
					Instruction ins = getInstruction(wholeLine);
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
	 * @param line
	 * @return
	 */
	public Instruction getInstruction(String line) {
		if (line.equals(""))
			return null;
		SMLInstrcutions smlIns = new SMLInstrcutions(line);
		String ins = smlIns.getInstruction();
		int noOfArgs =0;
		
		boolean isTypeMatch=false;
		try {
			Class<?> cls = Class.forName(getClassName(ins));			
			Constructor<?>[] constructors = cls.getConstructors();
			for(Constructor<?> ctr : constructors){
				Class<?>[] consPropTypes = ctr.getParameterTypes();
				Object[] argsObj = smlIns.getConstructionArgs();
				noOfArgs = smlIns.getNumberOfConsArgs();
				if(noOfArgs == consPropTypes.length){
					isTypeMatch = smlIns.checkInstructionType(consPropTypes);
					if(isTypeMatch){
						return (Instruction)ctr.newInstance(argsObj);
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SecurityException | IllegalArgumentException | InvocationTargetException  e) {
			e.printStackTrace();
		}
		return null;
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