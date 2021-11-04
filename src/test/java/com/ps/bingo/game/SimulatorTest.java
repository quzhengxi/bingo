package com.ps.bingo.game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ps.bingo.Constants;
import com.ps.bingo.Simulator;
import com.ps.bingo.SimulatorMessageBundle;
import com.ps.bingo.validator.ValidatorMessageBundle;

public class SimulatorTest extends CommonTest{
	private final String TEN_N_COMMANDS = "\nN\nN\nN\nN\nN\nN\nN\nN\nN\nN\n";
	private final String DATA = "10\n1\n3X10\n3";

	private ByteArrayInputStream in = new ByteArrayInputStream((DATA + TEN_N_COMMANDS).getBytes());
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	private final ByteArrayOutputStream err = new ByteArrayOutputStream();

	private final InputStream stdin = System.in;
	private final PrintStream stdout = System.out;
	private final PrintStream stderr = System.err;
	@BeforeEach
	public void setUpStreams() {
		super.setup();
		System.setIn(in);
		System.setOut(new PrintStream(out));
		System.setErr(new PrintStream(err));
	}

	@AfterEach
	public void restoreStreams() {
		System.setIn(stdin);
		System.setOut(stdout);
		System.setErr(stderr);
	}

	@Test
	public void testSimulator() {
		Simulator.main(new String[0]);
		String printedContent = new String(out.toByteArray());
		assertTrue(printedContent.contains(SimulatorMessageBundle.WELCOME.format()));
		assertTrue(printedContent.contains(SimulatorMessageBundle.GAME_OVER.format()));
	}
	
	@Test
	public void testSimulator_ValidNumberRange_1() {
		String input = "1\n3\n1X10\n1";
		in = new ByteArrayInputStream((input + TEN_N_COMMANDS).getBytes());
		System.setIn(in);
		Simulator.main(new String[0]);
		String printedContent = new String(out.toByteArray());
		assertTrue(printedContent.contains(SimulatorMessageBundle.WELCOME.format()));
		assertTrue(printedContent.contains(SimulatorMessageBundle.GAME_OVER.format()));
	}
	@Test
	public void testSimulator_ValidNumberRange_100() {
		String input = "100\n3\n3X10\n3";
		StringBuilder nextCommands =new StringBuilder();
		for(int i=0; i<10; i++)
			nextCommands.append(TEN_N_COMMANDS);
		in = new ByteArrayInputStream((input + nextCommands.toString()).getBytes());
		System.setIn(in);
		Simulator.main(new String[0]);
		String printedContent = new String(out.toByteArray());
		assertTrue(printedContent.contains(SimulatorMessageBundle.WELCOME.format()));
		assertTrue(printedContent.contains(SimulatorMessageBundle.GAME_OVER.format()));
	}
	
	@Test
	public void testSimulator_InvalidNumberRange_0() {
		String input = "0\n10\n3\n3X10\n3";
		in = new ByteArrayInputStream((input + TEN_N_COMMANDS).getBytes());
		System.setIn(in);
		Simulator.main(new String[0]);
		String printedContent = new String(out.toByteArray());
		assertTrue(printedContent.contains(SimulatorMessageBundle.WELCOME.format()));
		assertTrue(printedContent.contains(SimulatorMessageBundle.GAME_OVER.format()));
		String printedErrContent = new String(err.toByteArray());		
		assertTrue(printedErrContent.contains(ValidatorMessageBundle.INVALID_NUMBER_RANGE.format(0, Constants.MAX_NUMBER_RANGE)));
	}
	
	@Test
	public void testSimulator_InvalidNumberRange_101() {
		String input = "101\n10\n3\n3X10\n3";
		in = new ByteArrayInputStream((input + TEN_N_COMMANDS).getBytes());
		System.setIn(in);
		Simulator.main(new String[0]);
		String printedContent = new String(out.toByteArray());
		assertTrue(printedContent.contains(SimulatorMessageBundle.WELCOME.format()));
		assertTrue(printedContent.contains(SimulatorMessageBundle.GAME_OVER.format()));
		String printedErrContent = new String(err.toByteArray());		
		assertTrue(printedErrContent.contains(ValidatorMessageBundle.INVALID_NUMBER_RANGE.format(101, Constants.MAX_NUMBER_RANGE)));
	}
	
	
}
